package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium

@Composable
fun CommercialDealTypeSection(
    selectedType: DealType?,
    onTypeSelected: (DealType?) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge),
        horizontalArrangement = Arrangement.spacedBy(PaddingMedium)
    ) {

        DealType.entries.forEach { type ->
            val textRes = when (type) {
                DealType.BUY -> R.string.buy
                DealType.RENT -> R.string.rent
            }

            SellerFilterChip(
                text = stringResource(textRes),
                selected = selectedType == type,
                onClick = { onTypeSelected(type.takeIf { selectedType != type }) }
            )
        }
    }

}