package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.repository.IPropertyRepository

class DeletePropertyUseCase(
    private val propertyRepository: IPropertyRepository,
) {

    suspend operator fun invoke(propertyId: Int): Resource<Unit, String> {
        return try {
            propertyRepository.deleteProperty(propertyId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}

