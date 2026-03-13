package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.FiltersProperty
import kotlinx.coroutines.flow.Flow

interface IFiltersRepository {
    fun getSelectedFiltersMarker(): Flow<FiltersProperty?>
    suspend fun saveSelectedFiltersMarker(marker: FiltersProperty)
    suspend fun clearSelectedFiltersMarker()
}