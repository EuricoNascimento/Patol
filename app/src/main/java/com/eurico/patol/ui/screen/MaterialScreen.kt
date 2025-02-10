package com.eurico.patol.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eurico.patol.model.ContentType
import com.eurico.patol.model.Content

@Composable
fun MaterialScreen() {

}

@Composable
fun TopicList(
    contents: List<Content>
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(contents) { content ->
            when (content.contentType) {
                ContentType.TEXT -> TODO()
                ContentType.IMAGE_TEXT -> TODO()
            }
        }
    }
}