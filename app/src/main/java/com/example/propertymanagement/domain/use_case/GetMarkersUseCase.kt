package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.PropertyMarker
import com.example.propertymanagement.domain.repository.IPropertyRepository

class GetMarkersUseCase(
    private val propertyRepository: IPropertyRepository
) {

    suspend operator fun invoke(): List<PropertyMarker> {
        return propertyRepository.getMarkers()
    }

}