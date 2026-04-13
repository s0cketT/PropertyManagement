package com.example.propertymanagement.ui.profile_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.ui.theme.IconSizeProfile
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.TextMedium
import com.example.propertymanagement.ui.theme.VerticalPaddingItem

@Composable
fun ProfileActionItem(
    textRes: Int,
    icon: ImageVector,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textTint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = PaddingLarge, vertical = VerticalPaddingItem),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(IconSizeProfile)
        )

        Spacer(modifier = Modifier.width(SpacerBetweenElements))

        Text(
            text = stringResource(textRes),
            fontSize = TextMedium,
            color = textTint,
            modifier = Modifier.weight(1f)
        )
    }
}