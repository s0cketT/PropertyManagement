package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.imageItem

@Composable
fun ImageItem(uri: ByteArray) {
    AsyncImage(
        model = uri,
        contentDescription = null,
        modifier = Modifier
            .size(imageItem)
            .padding(PaddingSmall)
            .clip(RoundedCornerShape(ButtonCornerRadius)),
        contentScale = ContentScale.Crop
    )
}