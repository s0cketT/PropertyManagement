package com.example.propertymanagement.ui.profile_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.profile_screen.ProfileIntent
import com.example.propertymanagement.ui.profile_screen.ProfileState
import com.example.propertymanagement.ui.theme.CardElevationLow
import com.example.propertymanagement.ui.theme.IconSizeProfile
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.ProfileSectionCardElevation
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerSmall

@Composable
fun AuthorizedContent(
    state: ProfileState,
    intent: (ProfileIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        Spacer(modifier = Modifier.height(PaddingMedium))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
        ) {
            ProfileHeader(
                user = state.user,
                onClick = { intent(ProfileIntent.PersonalInfo) },
            )
        }

        Spacer(modifier = Modifier.height(SpacerMedium))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
        ) {
            Column {
                ProfilePublishCallout(
                    onClick = { intent(ProfileIntent.NavToPublish) },
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = PaddingLarge),
                    thickness = CardElevationLow,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                ProfileActionItem(
                    textRes = R.string.my_ads,
                    icon = Icons.AutoMirrored.Filled.List,
                    onClick = { intent(ProfileIntent.MyAds) },
                )
            }
        }

        Spacer(modifier = Modifier.height(SpacerMedium))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
        ) {
            Column {
                ProfileActionItem(
                    textRes = R.string.rate_app,
                    icon = Icons.Default.Star,
                    trailingText = state.user?.appRating?.let { "$it/5" },
                    onClick = { intent(ProfileIntent.RateApp) },
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = PaddingLarge),
                    thickness = CardElevationLow,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                ProfileActionItem(
                    textRes = R.string.settings_app,
                    icon = Icons.Default.Settings,
                    onClick = { intent(ProfileIntent.Settings) },
                )
            }
        }

        Spacer(modifier = Modifier.height(SpacerSmall))
    }
}

@Composable
private fun ProfilePublishCallout(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = PaddingMedium, end = PaddingMedium, top = PaddingMedium, bottom = PaddingMedium)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick)
            .padding(PaddingLarge),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(IconSizeProfile + 4.dp),
        )

        Text(
            text = stringResource(R.string.apply_request),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .weight(1f)
                .padding(start = PaddingMedium),
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f),
        )
    }
}
