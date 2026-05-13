package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.currency.convertAmountBetweenCurrencies
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.pricing.ManagerCommissionPricing
import com.example.propertymanagement.domain.pricing.formatCommissionPercentForDisplay
import com.example.propertymanagement.ui.common.formatPrice
import com.example.propertymanagement.ui.mapper.symbol
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailIntent
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailState
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailApplicationBottomSheet(
    state: PropertyDetailState,
    intent: (PropertyDetailIntent) -> Unit,
    onDismiss: () -> Unit
) {
    val property = state.property ?: return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge)
                .padding(bottom = PaddingLarge)
        ) {
            Text(
                text = stringResource(R.string.property_detail_application_sheet_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            val percent = state.managerCommissionPercent
            if (percent > 0.0) {
                val leadCurrency = state.detailPriceLeadCurrency
                val rates = state.currencyRates
                val commissionDisplayPair = remember(
                    property.price,
                    property.currency,
                    percent,
                    leadCurrency,
                    rates,
                ) {
                    val managerShareListed = ManagerCommissionPricing.commissionAmount(
                        listedPrice = property.price,
                        commissionPercent = percent,
                    )
                    val canConvert =
                        rates[CurrencyType.USD.name] != null &&
                            rates[CurrencyType.EUR.name] != null
                    if (!canConvert) {
                        managerShareListed to property.currency
                    } else {
                        val converted = convertAmountBetweenCurrencies(
                            amount = managerShareListed,
                            from = property.currency,
                            to = leadCurrency,
                            rates = rates,
                        )
                        converted to leadCurrency
                    }
                }
                Text(
                    text = stringResource(
                        R.string.property_application_commission_notice,
                        formatCommissionPercentForDisplay(percent),
                        formatPrice(commissionDisplayPair.first),
                        commissionDisplayPair.second.symbol(),
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(SpacerMedium))
            }

            OutlinedTextField(
                value = state.applicationComment,
                onValueChange = { intent(PropertyDetailIntent.SetApplicationComment(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.property_detail_application_comment_label)) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.property_detail_application_comment_placeholder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                    )
                },
                minLines = 3,
                maxLines = 8,
                enabled = !state.isSubmittingApplication,
                shape = RoundedCornerShape(ButtonCornerRadius),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                ),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            Button(
                onClick = { intent(PropertyDetailIntent.ConfirmApplicationSubmit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmittingApplication
            ) {
                Text(
                    text = stringResource(
                        if (state.isSubmittingApplication) {
                            R.string.property_detail_application_submitting
                        } else {
                            R.string.property_detail_application_submit
                        }
                    )
                )
            }
        }
    }
}
