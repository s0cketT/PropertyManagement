package com.example.propertymanagement.ui.personal_info_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.propertymanagement.ui.theme.AvatarLargeIconSize
import com.example.propertymanagement.ui.theme.AvatarLargeSize
import com.example.propertymanagement.ui.theme.IconSizeProfile
import com.example.propertymanagement.ui.theme.IconSmall

@Composable
fun ProfileInfoRow(
    imageUrl: String?,
    avatarBytes: ByteArray?,
    isAvatarRemoved: Boolean,
    onAvatarClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AvatarBlock(
            imageUrl = imageUrl,
            avatarBytes = avatarBytes,
            isAvatarRemoved = isAvatarRemoved,
            onAvatarClick = onAvatarClick,
            onRemoveClick = onRemoveClick,
        )
    }
}

@Composable
private fun AvatarBlock(
    imageUrl: String?,
    avatarBytes: ByteArray?,
    isAvatarRemoved: Boolean,
    onAvatarClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    val hasAvatar =
        avatarBytes != null ||
            (!imageUrl.isNullOrBlank() && !isAvatarRemoved)

    Box(
        modifier = Modifier.size(AvatarLargeSize),
    ) {

        AvatarLargeRing(
            imageUrl = imageUrl,
            imageBytes = avatarBytes,
            isAvatarRemoved = isAvatarRemoved,
            modifier = Modifier
                .matchParentSize()
                .clickable(onClick = onAvatarClick),
        )

        if (hasAvatar) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(IconSizeProfile)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                        shape = CircleShape,
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = onRemoveClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(IconSmall),
                )
            }
        }
    }
}

@Composable
private fun AvatarLargeRing(
    imageUrl: String?,
    imageBytes: ByteArray?,
    isAvatarRemoved: Boolean,
    modifier: Modifier = Modifier,
) {
    var isError by remember { mutableStateOf(false) }

    val ringColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)

    Box(
        modifier = modifier
            .border(width = 2.dp, color = ringColor, shape = CircleShape)
            .padding(3.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        when {
            imageBytes != null -> {
                AsyncImage(
                    model = imageBytes,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            isAvatarRemoved || imageUrl.isNullOrBlank() || isError -> {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(AvatarLargeIconSize),
                )
            }

            else -> {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                    onError = { isError = true },
                )
            }
        }
    }
}
