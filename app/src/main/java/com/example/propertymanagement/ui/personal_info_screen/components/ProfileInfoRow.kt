package com.example.propertymanagement.ui.personal_info_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.ui.mapper.asString
import com.example.propertymanagement.ui.theme.AvatarLargeIconSize
import com.example.propertymanagement.ui.theme.AvatarLargeSize
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.IconSmall
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerTiny
import com.example.propertymanagement.ui.theme.TextMedium
import com.example.propertymanagement.ui.theme.TextSmall

@Composable
fun ProfileInfoRow(
    user: UserProfile?,
    avatarBytes: ByteArray?,
    isAvatarRemoved: Boolean,
    onAvatarClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AvatarBlock(
            imageUrl = user?.avatarUrl,
            avatarBytes = avatarBytes,
            isAvatarRemoved = isAvatarRemoved,
            onAvatarClick = onAvatarClick,
            onRemoveClick = onRemoveClick
        )

        Spacer(modifier = Modifier.size(SpacerMedium))

        Column {
            Text(
                text = user?.name.orEmpty(),
                fontSize = TextMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(SpacerTiny))

            Text(
                text = user?.sellerType?.asString().orEmpty(),
                fontSize = TextSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AvatarBlock(
    imageUrl: String?,
    avatarBytes: ByteArray?,
    isAvatarRemoved: Boolean,
    onAvatarClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val hasAvatar =
        avatarBytes != null ||
                (!imageUrl.isNullOrBlank() && !isAvatarRemoved)

    Box(
        modifier = Modifier.size(AvatarLargeSize)
    ) {

        AvatarLarge(
            imageUrl = imageUrl,
            imageBytes = avatarBytes,
            isAvatarRemoved = isAvatarRemoved,
            modifier = Modifier
                .matchParentSize()
                .clickable { onAvatarClick() }
        )

        if (hasAvatar) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    )
                    .clickable { onRemoveClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(IconSmall)
                )
            }
        }
    }
}

@Composable
private fun AvatarLarge(
    imageUrl: String? = "",
    imageBytes: ByteArray? = null,
    isAvatarRemoved: Boolean,
    modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(ButtonCornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {

        when {
            imageBytes != null -> {
                AsyncImage(
                    model = imageBytes,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }

            isAvatarRemoved || imageUrl.isNullOrBlank() || isError -> {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(AvatarLargeIconSize)
                )
            }

            else -> {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                    onError = { isError = true }
                )
            }
        }
    }
}
