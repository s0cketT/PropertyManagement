package com.example.propertymanagement.ui.profile_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.profile_screen.ProfileIntent
import com.example.propertymanagement.ui.profile_screen.ProfileState
import com.example.propertymanagement.ui.theme.BoxGrayHeight
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.IconSmall
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerTiny
import com.example.propertymanagement.ui.theme.TextMedium
import com.example.propertymanagement.ui.theme.VerticalPaddingItem

@Composable
fun AuthorizedContent(
    state: ProfileState,
    intent: (ProfileIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileHeader(
            user = state.user,
            onClick = { intent(ProfileIntent.PersonalInfo) }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BoxGrayHeight)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        ProfileActionItem(
            textRes = R.string.apply_request,
            icon = Icons.Default.AddCircle,
            iconTint = MaterialTheme.colorScheme.primary,
            textTint = MaterialTheme.colorScheme.primary,
            onClick = { intent(ProfileIntent.NavToPublish) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(start = PaddingLarge, end = VerticalPaddingItem),
            thickness = DividerThickness,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ProfileActionItem(
            textRes = R.string.my_ads,
            icon = Icons.AutoMirrored.Filled.List,
            onClick = { intent(ProfileIntent.MyAds) }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BoxGrayHeight)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        ProfileActionItem(
            textRes = R.string.rate_app,
            icon = Icons.Default.Star,
            onClick = { intent(ProfileIntent.RateApp) }
        )

        ProfileActionItem(
            textRes = R.string.settings_app,
            icon = Icons.Default.Settings,
            onClick = { intent(ProfileIntent.Settings) }
        )
    }
}