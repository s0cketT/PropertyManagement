package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.components.LabeledCheckboxSection
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.filters_screen.FiltersState
import com.example.propertymanagement.ui.theme.BoxGrayHeight
import com.example.propertymanagement.ui.theme.PaddingLarge

@Composable
fun BaseInfoSectionBottomFilters(
    state: FiltersState,
    intent: (FiltersIntent) -> Unit
) {
    Spacer(modifier = Modifier.height(PaddingLarge))

    LabeledCheckboxSection(
        text = stringResource(R.string.filter_only_with_photos),
        isChecked = state.onlyWithPhotos,
        onCheckedChange = { intent(FiltersIntent.OnlyWithPhotosChanged(it)) }
    )

    Box(modifier = Modifier.fillMaxWidth().height(BoxGrayHeight).background(MaterialTheme.colorScheme.surface))

    Spacer(modifier = Modifier.height(PaddingLarge))

    SortingSection(
        selectedSort = state.sortType,
        onSortSelected = { intent(FiltersIntent.SortChanged(it)) }
    )
}