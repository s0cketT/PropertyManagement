package com.example.propertymanagement.ui.publish_screen.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.ChipCornerRadius
import com.example.propertymanagement.ui.theme.IconSizeActionSquare
import com.example.propertymanagement.ui.theme.ImagePickerHeight
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.SpacerSmall

@Composable
fun ImagePickerCard(
    newImageBytes: List<ByteArray>,
    existingImageUrls: List<String> = emptyList(),
    onClick: () -> Unit,
) {

    val firstBytes = newImageBytes.firstOrNull()
    val firstUrl = existingImageUrls.firstOrNull()
    val totalCount = existingImageUrls.size + newImageBytes.size

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge)
            .height(ImagePickerHeight)
            .clip(RoundedCornerShape(ButtonCornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        firstBytes?.let { bytes ->

            val bitmap = BitmapFactory.decodeByteArray(
                bytes,
                0,
                bytes.size
            )

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.scrim
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(PaddingMedium)
                    .background(
                        MaterialTheme.colorScheme.scrim,
                        RoundedCornerShape(ChipCornerRadius)
                    )
                    .padding(
                        horizontal = PaddingMedium,
                        vertical = PaddingSmall
                    )
            ) {
                Text(
                    text = stringResource(R.string.photos_count, totalCount),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (firstBytes == null) {
            firstUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.scrim
                                )
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(PaddingMedium)
                        .background(
                            MaterialTheme.colorScheme.scrim,
                            RoundedCornerShape(ChipCornerRadius)
                        )
                        .padding(
                            horizontal = PaddingMedium,
                            vertical = PaddingSmall
                        )
                ) {
                    Text(
                        text = stringResource(R.string.photos_count, totalCount),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        if (firstBytes == null && firstUrl == null) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(IconSizeActionSquare)
                )

                Spacer(modifier = Modifier.height(SpacerSmall))

                Text(
                    text = stringResource(R.string.add_photo),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}