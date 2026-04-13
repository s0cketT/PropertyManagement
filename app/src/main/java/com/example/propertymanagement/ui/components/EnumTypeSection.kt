package com.example.propertymanagement.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.ui.filters_screen.components.SellerFilterChip
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium

@Composable
fun <T : Enum<T>> EnumTypeSection(
    title: String,
    entries: Array<T>,
    selectedType: T?,
    onTypeSelected: (T?) -> Unit,
    titleRes: (T) -> Int
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(PaddingMedium)
        ) {

            entries.forEach { type ->

                SellerFilterChip(
                    text = stringResource(titleRes(type)),
                    selected = selectedType == type,
                    onClick = {
                        if (selectedType == type) {
                            onTypeSelected(null)
                        } else {
                            onTypeSelected(type)
                        }
                    }
                )
            }
        }
    }
}