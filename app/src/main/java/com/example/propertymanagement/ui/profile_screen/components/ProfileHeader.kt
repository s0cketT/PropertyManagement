package com.example.propertymanagement.ui.profile_screen.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import coil.compose.AsyncImage
import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.ui.mapper.asString
import com.example.propertymanagement.ui.theme.AvatarSize
import com.example.propertymanagement.ui.theme.IconSizeArrowLarge
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.SpacerTiny
import com.example.propertymanagement.ui.theme.TextLarge
import com.example.propertymanagement.ui.theme.TextRegular
import com.example.propertymanagement.ui.theme.VerticalPaddingItem

@Composable
fun ProfileHeader(
    user: UserProfile?,
    onClick: () -> Unit = {}
) {
    if (user == null) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = PaddingLarge, vertical = VerticalPaddingItem),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(imageUrl = user.avatarUrl)

        Spacer(modifier = Modifier.width(SpacerBetweenElements))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.name,
                fontSize = TextLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(SpacerTiny))

            Text(
                text = user.sellerType.asString(),
                fontSize = TextRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(IconSizeArrowLarge)
        )
    }
}

@Composable
private fun Avatar(
    imageUrl: String? = ""
) {
    var isError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(AvatarSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {

        if (imageUrl.isNullOrBlank() || isError) {
            DefaultAvatar()
        } else {
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

@Composable
private fun DefaultAvatar() {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(AvatarSize / 2)
    )
}