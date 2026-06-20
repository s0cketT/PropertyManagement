package com.example.propertymanagement.ui.auth_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.BottomSheetTopCornerRadius
import com.example.propertymanagement.ui.theme.IconSizeActionSquare
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.ProfileSectionCardElevation
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    if (!visible) {
        return
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val maxSheetHeight = LocalConfiguration.current.screenHeightDp * 0.92f

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = BottomSheetTopCornerRadius,
            topEnd = BottomSheetTopCornerRadius,
        ),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxSheetHeight.dp)
                .padding(horizontal = PaddingLarge)
                .padding(bottom = PaddingLarge),
        ) {
            PrivacyPolicySheetHeader()

            Spacer(modifier = Modifier.height(SpacerMedium))

            PrivacyPolicyTrustHighlights()

            Spacer(modifier = Modifier.height(SpacerMedium))

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(SpacerSmall),
            ) {
                PrivacyPolicyTopicCard(
                    icon = Icons.Outlined.Person,
                    titleRes = R.string.privacy_policy_section_data_title,
                    bodyRes = R.string.privacy_policy_section_data_body,
                )

                PrivacyPolicyTopicCard(
                    icon = Icons.Outlined.Visibility,
                    titleRes = R.string.privacy_policy_section_purpose_title,
                    bodyRes = R.string.privacy_policy_section_purpose_body,
                )

                PrivacyPolicyTopicCard(
                    icon = Icons.Outlined.Lock,
                    titleRes = R.string.privacy_policy_section_storage_title,
                    bodyRes = R.string.privacy_policy_section_storage_body,
                )

                PrivacyPolicyTopicCard(
                    icon = Icons.Outlined.VerifiedUser,
                    titleRes = R.string.privacy_policy_section_rights_title,
                    bodyRes = R.string.privacy_policy_section_rights_body,
                )

                PrivacyPolicyTopicCard(
                    icon = Icons.Outlined.Email,
                    titleRes = R.string.privacy_policy_section_contact_title,
                    bodyRes = R.string.privacy_policy_section_contact_body,
                )
            }

            Spacer(modifier = Modifier.height(SpacerMedium))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.privacy_policy_got_it))
            }
        }
    }
}

@Composable
private fun PrivacyPolicySheetHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(IconSizeActionSquare)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(PaddingLarge),
            )
        }

        Spacer(modifier = Modifier.height(PaddingMedium))

        Text(
            text = stringResource(R.string.privacy_policy_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(PaddingSmall))

        Text(
            text = stringResource(R.string.privacy_policy_intro),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PrivacyPolicyTrustHighlights() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
    ) {
        Column(
            modifier = Modifier.padding(PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(PaddingSmall),
        ) {
            PrivacyPolicyTrustItem(textRes = R.string.privacy_policy_trust_no_sale)
            PrivacyPolicyTrustItem(textRes = R.string.privacy_policy_trust_purpose)
            PrivacyPolicyTrustItem(textRes = R.string.privacy_policy_trust_secure)
        }
    }
}

@Composable
private fun PrivacyPolicyTrustItem(
    textRes: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(PaddingMedium),
        )

        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun PrivacyPolicyTopicCard(
    icon: ImageVector,
    titleRes: Int,
    bodyRes: Int,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
    ) {
        Row(
            modifier = Modifier.padding(PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(PaddingMedium),
        ) {
            Box(
                modifier = Modifier
                    .size(IconSizeActionSquare)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(PaddingLarge),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.height(PaddingSmall / 2))

                Text(
                    text = stringResource(bodyRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
