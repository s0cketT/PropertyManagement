package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.GeosuggestAddressField
import com.example.propertymanagement.domain.model.GeosuggestItem
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyIntent
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyState
import com.example.propertymanagement.ui.publish_screen.PublishIntent
import com.example.propertymanagement.ui.publish_screen.PublishState
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerLarge
import com.example.propertymanagement.ui.theme.SpacerMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishAddressBottomSheet(
    state: PublishState,
    intent: (PublishIntent) -> Unit,
    onDismiss: () -> Unit,
    onConfirmAddress: () -> Unit
) {
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
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.publish_address_sheet_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PublishGeosuggestAddressField(
                field = GeosuggestAddressField.COUNTRY,
                value = state.addressCountry,
                placeholder = stringResource(R.string.publish_address_country),
                activeField = state.activeGeosuggestField,
                suggestions = state.geosuggestSuggestions,
                isLoading = state.isGeosuggestLoading,
                onValueChange = { intent(PublishIntent.SetAddressCountry(it)) },
                onSuggestionClick = {
                    intent(
                        PublishIntent.SelectAddressSuggestion(
                            field = GeosuggestAddressField.COUNTRY,
                            value = it,
                        ),
                    )
                },
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PublishGeosuggestAddressField(
                field = GeosuggestAddressField.REGION,
                value = state.addressRegion,
                placeholder = stringResource(R.string.publish_address_region),
                activeField = state.activeGeosuggestField,
                suggestions = state.geosuggestSuggestions,
                isLoading = state.isGeosuggestLoading,
                onValueChange = { intent(PublishIntent.SetAddressRegion(it)) },
                onSuggestionClick = {
                    intent(
                        PublishIntent.SelectAddressSuggestion(
                            field = GeosuggestAddressField.REGION,
                            value = it,
                        ),
                    )
                },
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PublishGeosuggestAddressField(
                field = GeosuggestAddressField.CITY,
                value = state.addressCity,
                placeholder = stringResource(R.string.publish_address_city),
                activeField = state.activeGeosuggestField,
                suggestions = state.geosuggestSuggestions,
                isLoading = state.isGeosuggestLoading,
                onValueChange = { intent(PublishIntent.SetAddressCity(it)) },
                onSuggestionClick = {
                    intent(
                        PublishIntent.SelectAddressSuggestion(
                            field = GeosuggestAddressField.CITY,
                            value = it,
                        ),
                    )
                },
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PublishGeosuggestAddressField(
                field = GeosuggestAddressField.STREET,
                value = state.addressStreet,
                placeholder = stringResource(R.string.publish_address_street),
                activeField = state.activeGeosuggestField,
                suggestions = state.geosuggestSuggestions,
                isLoading = state.isGeosuggestLoading,
                onValueChange = { intent(PublishIntent.SetAddressStreet(it)) },
                onSuggestionClick = {
                    intent(
                        PublishIntent.SelectAddressSuggestion(
                            field = GeosuggestAddressField.STREET,
                            value = it,
                        ),
                    )
                },
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PropertyTitleTextField(
                value = state.addressHouse,
                placeholder = stringResource(R.string.publish_address_house),
                onValueChange = { intent(PublishIntent.SetAddressHouse(it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(SpacerLarge))

            OutlinedButton(
                onClick = {
                    intent(PublishIntent.SetAddressBottomSheetOpen(open = false))
                    intent(PublishIntent.SetMapPickerOpen(open = true))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.publish_pick_on_map))
            }

            Spacer(modifier = Modifier.height(SpacerMedium))

            Button(
                onClick = onConfirmAddress,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.publish_address_done))
            }

            Spacer(modifier = Modifier.height(SpacerMedium))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishAddressBottomSheet(
    state: EditPropertyState,
    intent: (EditPropertyIntent) -> Unit,
    onDismiss: () -> Unit,
    onConfirmAddress: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge)
                .padding(bottom = PaddingLarge)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(R.string.publish_address_sheet_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PropertyTitleTextField(
                value = state.addressCountry,
                placeholder = stringResource(R.string.publish_address_country),
                onValueChange = { intent(EditPropertyIntent.SetAddressCountry(it)) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PropertyTitleTextField(
                value = state.addressRegion,
                placeholder = stringResource(R.string.publish_address_region),
                onValueChange = { intent(EditPropertyIntent.SetAddressRegion(it)) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PropertyTitleTextField(
                value = state.addressCity,
                placeholder = stringResource(R.string.publish_address_city),
                onValueChange = { intent(EditPropertyIntent.SetAddressCity(it)) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PropertyTitleTextField(
                value = state.addressStreet,
                placeholder = stringResource(R.string.publish_address_street),
                onValueChange = { intent(EditPropertyIntent.SetAddressStreet(it)) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PropertyTitleTextField(
                value = state.addressHouse,
                placeholder = stringResource(R.string.publish_address_house),
                onValueChange = { intent(EditPropertyIntent.SetAddressHouse(it)) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(SpacerLarge))

            OutlinedButton(
                onClick = {
                    intent(EditPropertyIntent.SetAddressBottomSheetOpen(open = false))
                    intent(EditPropertyIntent.SetMapPickerOpen(open = true))
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.publish_pick_on_map))
            }

            Spacer(modifier = Modifier.height(SpacerMedium))

            Spacer(modifier = Modifier.height(SpacerMedium))
        }
    }
}

@Composable
private fun PublishGeosuggestAddressField(
    field: GeosuggestAddressField,
    value: String,
    placeholder: String,
    activeField: GeosuggestAddressField?,
    suggestions: List<GeosuggestItem>,
    isLoading: Boolean,
    onValueChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
) {
    val isActiveField = activeField == field
    GeosuggestTextField(
        value = value,
        placeholder = placeholder,
        suggestions = if (isActiveField) suggestions else emptyList(),
        isLoading = isActiveField && isLoading,
        onValueChange = onValueChange,
        onSuggestionClick = onSuggestionClick,
        modifier = Modifier.fillMaxWidth(),
    )
}
