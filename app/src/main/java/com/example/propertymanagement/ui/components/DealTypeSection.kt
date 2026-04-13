package com.example.propertymanagement.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.ui.filters_screen.components.SellerFilterChip
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium

@Composable
fun DealTypeSection(
    selectedType: DealType?,
    onTypeSelected: (DealType?) -> Unit,
    titleRes: (DealType) -> Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge),
        horizontalArrangement = Arrangement.spacedBy(PaddingMedium)
    ) {

        DealType.entries.forEach { type ->

            SellerFilterChip(
                text = stringResource(titleRes(type)),
                selected = selectedType == type,
                onClick = { onTypeSelected(type.takeIf { selectedType != type }) }
            )
        }
    }
}