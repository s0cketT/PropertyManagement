package com.example.propertymanagement.ui.profile_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.example.propertymanagement.ui.theme.HorizontalPadding
import com.example.propertymanagement.ui.theme.IconSizeProfile
import com.example.propertymanagement.ui.theme.ProfileMenuIconContainerSize
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.SurfaceTonalElevationLow
import com.example.propertymanagement.ui.theme.VerticalPaddingItem

@Composable
fun ProfileActionItem(
    textRes: Int,
    icon: ImageVector,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    textTint: Color = MaterialTheme.colorScheme.onSurface,
    trailingText: String? = null,
    horizontalContentPadding: Dp = HorizontalPadding,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = horizontalContentPadding, vertical = VerticalPaddingItem),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(ProfileMenuIconContainerSize),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
            tonalElevation = SurfaceTonalElevationLow,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(IconSizeProfile),
                )
            }
        }

        Spacer(modifier = Modifier.width(SpacerBetweenElements))

        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = textTint,
            modifier = Modifier.weight(1f),
        )

        trailingText?.let { suffix ->
            Text(
                text = suffix,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
