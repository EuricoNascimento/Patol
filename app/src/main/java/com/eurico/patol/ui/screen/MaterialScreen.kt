package com.eurico.patol.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eurico.patol.R
import com.eurico.patol.model.ContentType
import com.eurico.patol.model.database.ContentDTO
import com.eurico.patol.ui.screen.components.RotatingIcon
import com.eurico.patol.ui.screen.components.ScaleText
import com.eurico.patol.viewmodel.MaterialViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MaterialScreen(
    materialId: Long?,
    viewModel: MaterialViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initialize(materialId)
    }

    val enableLoading = viewModel.enableLoading.collectAsState()
    val errorMessage = viewModel.error.collectAsState()
    val contentList = viewModel.contentList.collectAsState()

    when {
        enableLoading.value.value -> RotatingIcon()
        errorMessage.value.value -> {}
        else -> ContentScreen(contentList.value.value)
    }


}

@Composable
fun ContentScreen(
    contentList: List<ContentDTO>
) {
    val context = LocalContext.current
    if (contentList.isEmpty()) {
        ScaleText(
            text = context.getString(R.string.no_content_available),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState(pageCount = { contentList.size })
        val coroutineScope = rememberCoroutineScope()
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimaryContainer, MaterialTheme.shapes.large)
                    .semantics {
                        contentDescription =
                            context.getString(R.string.pager_content_description)
                    }
            ) { page ->
                val content = contentList[page]
                ContentItem(content, page, context)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }, enabled = pagerState.currentPage > 0) {
                ScaleText(
                    text = stringResource(R.string.back_page, pagerState.currentPage),
                    fontSize = 8.sp
                )
            }
            Button(onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }, enabled = pagerState.currentPage < contentList.size - 1) {
                ScaleText(
                    text = stringResource(R.string.next_page, pagerState.currentPage + 1),
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun ContentItem(content: ContentDTO, page: Int, context: Context) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.large)
            .verticalScroll(rememberScrollState())
            .semantics {
                contentDescription = context.getString(R.string.content_item_description, page + 1)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (content.contentType) {
            ContentType.TEXT -> TextContent(content)
            ContentType.IMAGE_TEXT -> ImageTextContent(content)
        }
    }
}

@Composable
fun TextContent(content: ContentDTO) {
    ScaleText(
        text = content.text,
        modifier = Modifier
            .fillMaxSize(),
        overflow = TextOverflow.Ellipsis,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageTextContent(content: ContentDTO) {
    if (content.img != null) {
        val context = LocalContext.current
        GlideImage(
            model = content.img,
            contentDescription = stringResource(R.string.image_content_description, content.text),
            modifier = Modifier
                .size(300.dp)
                .border(1.dp, Color.Gray)
                .semantics{
                    contentDescription = context.getString(R.string.image_content_description, content.text)
                },
            contentScale = ContentScale.Fit,
        )
    } else {
        TextContent(content)
    }
}