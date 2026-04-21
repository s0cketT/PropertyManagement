package com.example.propertymanagement.ui.personal_info_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.ui.auth_screen.AuthError
import com.example.propertymanagement.ui.auth_screen.components.AuthBelarusPhoneTextField
import com.example.propertymanagement.ui.auth_screen.components.AuthTextField
import com.example.propertymanagement.ui.filters_screen.components.SellerFilterChip
import com.example.propertymanagement.ui.mapper.asString
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoState
import com.example.propertymanagement.ui.theme.CardElevationLow
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.example.propertymanagement.ui.theme.SpacerLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerSmall

@Composable
fun ProfileContent(
    state: PersonalInfoState,
    onNameChange: (String) -> Unit,
    onPhoneNationalChange: (String) -> Unit,
    onSellerTypeChange: (SellerType) -> Unit,
    onAvatarClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge),
        shape = RoundedCornerShape(PropertyDetailMapCornerRadius),
        tonalElevation = CardElevationLow,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileInfoRow(
                imageUrl = state.user?.avatarUrl,
                avatarBytes = state.avatarBytes,
                isAvatarRemoved = state.isAvatarRemoved,
                onAvatarClick = onAvatarClick,
                onRemoveClick = onRemoveClick,
            )

            Spacer(modifier = Modifier.height(SpacerLarge))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(R.string.profile_email),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(PaddingSmall))
                Text(
                    text = state.user?.email.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(modifier = Modifier.height(SpacerMedium))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(SpacerMedium))

            AuthTextField(
                value = state.editedName,
                placeholderRes = R.string.first_name,
                error = if (state.nameInvalid) AuthError.EmptyField else null,
                onValueChange = onNameChange,
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            Text(
                text = stringResource(R.string.phone),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = PaddingSmall),
            )
            AuthBelarusPhoneTextField(
                nationalDigits = state.editedPhoneNational,
                error = if (state.phoneInvalid) AuthError.InvalidPhone else null,
                onNationalDigitsChange = onPhoneNationalChange,
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            Text(
                text = stringResource(R.string.personal_info_seller_type_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(SpacerSmall))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PaddingMedium),
            ) {
                SellerType.entries.forEach { type ->
                    SellerFilterChip(
                        text = type.asString(),
                        selected = state.editedSellerType == type,
                        onClick = { onSellerTypeChange(type) },
                    )
                }
            }
        }
    }
}
