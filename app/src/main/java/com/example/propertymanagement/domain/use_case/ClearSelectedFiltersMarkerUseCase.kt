package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.IFiltersRepository

class ClearSelectedFiltersMarkerUseCase(
    private val filterRepository: IFiltersRepository
) {

    suspend operator fun invoke() {
        filterRepository.clearSelectedFiltersMarker()
    }

}