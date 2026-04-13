package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.ui.components.EnumRadioSection
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium

@Composable
fun SortingSection(
    selectedSort: SortType,
    onSortSelected: (SortType) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.sorting),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = PaddingLarge)
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        EnumRadioSection(
            entries = SortType.entries.toTypedArray(),
            selected = selectedSort,
            onSelected = onSortSelected,
            titleRes = { it.titleRes() }
        )
    }
}