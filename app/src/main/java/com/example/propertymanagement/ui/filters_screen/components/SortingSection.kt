package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.ui.theme.DividerColor
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.PrimaryBlue

@Composable
fun SortingSection(
    selectedSort: SortType,
    onSortSelected: (SortType) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.sorting),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = PaddingLarge)
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        SortType.entries.forEach { sortType ->

            val textRes = when (sortType) {
                SortType.NEWEST -> R.string.sort_newest
                SortType.PRICE_ASC -> R.string.sort_price_asc
                SortType.PRICE_DESC -> R.string.sort_price_desc
            }

            SortingItem(
                text = stringResource(textRes),
                selected = selectedSort == sortType,
                onClick = { onSortSelected(sortType) }
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = PaddingLarge),
                color = DividerColor,
                thickness = DividerThickness
            )
        }
    }
}

@Composable
private fun SortingItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = PaddingSmall, horizontal = PaddingLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryBlue
            )
        )
    }
}