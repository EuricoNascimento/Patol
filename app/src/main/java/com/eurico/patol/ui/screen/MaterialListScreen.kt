package com.eurico.patol.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.eurico.patol.model.database.MaterialDTO
import com.eurico.patol.ui.RouterSet
import com.eurico.patol.ui.screen.components.RotatingIcon
import com.eurico.patol.viewmodel.MaterialListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MaterialListScreen(
    navController: NavController,
    viewModel: MaterialListViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    val enableLoading = viewModel.enableLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    val materialList = viewModel.materialList.collectAsState()

    when{
        enableLoading.value.value -> RotatingIcon()
        errorMessage.value.value != 0 -> {}
        materialList.value.value.isNotEmpty() -> ListItem(
            materials = materialList.value.value,
            finishCallBack = {
                navController.navigate("${RouterSet.LIST_SCREEN.name}/$it")
            }
        )
    }
}

@Composable
fun ListItem(
    materials: List<MaterialDTO>,
    finishCallBack: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items (materials) { material ->
            ItemBoxDescription(material, finishCallBack)
        }
    }
}

@Composable
fun ItemBoxDescription(
    material: MaterialDTO,
    finishCallBack: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { finishCallBack.invoke(material.id) }
            .semantics(mergeDescendants = true) {}
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = material.title,
                fontSize = 48.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = material.subtitle,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}