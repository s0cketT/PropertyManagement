package com.example.propertymanagement.ui.filters_screen.region_selection

import com.example.propertymanagement.domain.model.Region

data class RegionSelectionState(
    val isLoading: Boolean = true,
    val regions: List<Region> = emptyList(),
    val error: Throwable? = null,
)

