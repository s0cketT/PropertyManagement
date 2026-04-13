package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.propertymanagement.ui.publish_screen.PublishIntent
import com.example.propertymanagement.ui.publish_screen.PublishState
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun ImagePickerSection(state: PublishState, intent: (PublishIntent) -> Unit) {

    ImagePickerCard(
        images = state.imageBytes,
        onClick = { intent(PublishIntent.PickImages) }
    )

    if (state.imageBytes.isNotEmpty()) {
        Spacer(modifier = Modifier.height(SpacerMedium))

        LazyRow(modifier = Modifier.padding(start = PaddingMedium, end = PaddingMedium)) {
            items(state.imageBytes) { ImageItem(it) }
        }
    }

    Spacer(modifier = Modifier.height(SpacerMedium))
}