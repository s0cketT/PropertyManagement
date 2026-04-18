package com.example.propertymanagement.ui.personal_info_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.ui.filters_screen.components.SellerFilterChip
import com.example.propertymanagement.ui.mapper.asString
import com.example.propertymanagement.ui.personal_info_screen.BelarusPhoneUtils
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoState
import com.example.propertymanagement.ui.theme.BoxGrayHeight
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.TextSmall

@Composable
fun ProfileContent(
    state: PersonalInfoState,
    onNameChange: (String) -> Unit,
    onPhoneNationalChange: (String) -> Unit,
    onSellerTypeChange: (SellerType) -> Unit,
    onAvatarClick: () -> Unit,
    onRemoveClick: () -> Unit
) {

    EmailRow(email = state.user?.email.orEmpty())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(BoxGrayHeight)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )

    Spacer(modifier = Modifier.height(SpacerMedium))

    ProfileInfoRow(
        imageUrl = state.user?.avatarUrl,
        avatarBytes = state.avatarBytes,
        isAvatarRemoved = state.isAvatarRemoved,
        onAvatarClick = onAvatarClick,
        onRemoveClick = onRemoveClick
    )

    Spacer(modifier = Modifier.height(SpacerMedium))

    OutlinedTextField(
        value = state.editedName,
        onValueChange = onNameChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge),
        label = { Text(stringResource(R.string.personal_info_name_label)) },
        singleLine = true,
        isError = state.nameInvalid,
        supportingText = if (state.nameInvalid) {
            { Text(stringResource(R.string.personal_info_name_error)) }
        } else null
    )

    Spacer(modifier = Modifier.height(SpacerMedium))

    BelarusPhoneField(
        nationalDigits = state.editedPhoneNational,
        onNationalDigitsChange = onPhoneNationalChange,
        isError = state.phoneInvalid,
        modifier = Modifier.padding(horizontal = PaddingLarge)
    )

    Spacer(modifier = Modifier.height(SpacerMedium))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge)
    ) {
        Text(
            text = stringResource(R.string.personal_info_seller_type_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(SpacerSmall))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingMedium)
        ) {
            SellerType.entries.forEach { type ->
                SellerFilterChip(
                    text = type.asString(),
                    selected = state.editedSellerType == type,
                    onClick = { onSellerTypeChange(type) }
                )
            }
        }
    }
}

@Composable
private fun BelarusPhoneField(
    nationalDigits: String,
    onNationalDigitsChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.personal_info_phone_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(SpacerSmall))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\uD83C\uDDE7\uD83C\uDDFE",
                fontSize = 28.sp,
                modifier = Modifier.padding(end = PaddingMedium)
            )
            Text(
                text = BelarusPhoneUtils.E164_PREFIX,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = PaddingMedium)
            )
            OutlinedTextField(
                value = nationalDigits,
                onValueChange = onNationalDigitsChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = stringResource(R.string.personal_info_phone_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = TextSmall
                    )
                },
                isError = isError,
                supportingText = if (isError) {
                    { Text(stringResource(R.string.personal_info_phone_error)) }
                } else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}
