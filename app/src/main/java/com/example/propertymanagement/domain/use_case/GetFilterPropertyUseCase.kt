package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.repository.IFiltersRepository
import kotlinx.coroutines.flow.Flow

class GetFilterPropertyUseCase(
    private val filtersRepository: IFiltersRepository
) {
    operator fun invoke(): Flow<FiltersProperty?> {
        return filtersRepository.getSelectedFiltersMarker()
    }
}