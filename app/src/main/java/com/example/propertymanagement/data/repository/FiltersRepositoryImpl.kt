package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.dao.FilterDao
import com.example.propertymanagement.data.mapper.toDomain
import com.example.propertymanagement.data.mapper.toEntity
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.repository.IFiltersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FiltersRepositoryImpl(
    private val filterDao: FilterDao
) : IFiltersRepository {

    override fun getSelectedFiltersMarker(): Flow<FiltersProperty?> {
        return filterDao.getSelectedMarker().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveSelectedFiltersMarker(marker: FiltersProperty) {
        filterDao.insert(marker.toEntity())
    }

    override suspend fun clearSelectedFiltersMarker() {
        filterDao.clearSelectedMarker()
    }
}