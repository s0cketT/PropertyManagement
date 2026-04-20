package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyIntent
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyState
import com.example.propertymanagement.ui.publish_screen.PublishIntent
import com.example.propertymanagement.ui.publish_screen.PublishState
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun ImagePickerSection(state: PublishState, intent: (PublishIntent) -> Unit) {

    ImagePickerCard(
        newImageBytes = state.imageBytes,
        existingImageUrls = emptyList(),
        onClick = { intent(PublishIntent.PickImages) },
    )

    val hasAnyPhoto = state.imageBytes.isNotEmpty()

    if (hasAnyPhoto) {
        Spacer(modifier = Modifier.height(SpacerMedium))

        TextButton(
            onClick = { intent(PublishIntent.ClearAllImages) },
            modifier = Modifier.padding(horizontal = PaddingLarge),
        ) {
            Text(text = stringResource(R.string.publish_clear_all_photos))
        }

        Spacer(modifier = Modifier.height(SpacerMedium))

        LazyRow(modifier = Modifier.padding(start = PaddingMedium, end = PaddingMedium)) {
            items(state.imageBytes) { ImageItem(it) }
        }
    }

    Spacer(modifier = Modifier.height(SpacerMedium))
}

@Composable
fun ImagePickerSection(state: EditPropertyState, intent: (EditPropertyIntent) -> Unit) {

    ImagePickerCard(
        newImageBytes = state.imageBytes,
        existingImageUrls = state.existingImageUrls,
        onClick = { intent(EditPropertyIntent.PickImages) },
    )

    val hasAnyPhoto =
        state.imageBytes.isNotEmpty() || state.existingImageUrls.isNotEmpty()

    if (hasAnyPhoto) {
        Spacer(modifier = Modifier.height(SpacerMedium))

        TextButton(
            onClick = { intent(EditPropertyIntent.ClearAllImages) },
            modifier = Modifier.padding(horizontal = PaddingLarge),
        ) {
            Text(text = stringResource(R.string.publish_clear_all_photos))
        }

        Spacer(modifier = Modifier.height(SpacerMedium))

        LazyRow(modifier = Modifier.padding(start = PaddingMedium, end = PaddingMedium)) {
            items(state.existingImageUrls) { PublishImageUrlItem(it) }
            items(state.imageBytes) { ImageItem(it) }
        }
    }

    Spacer(modifier = Modifier.height(SpacerMedium))
}