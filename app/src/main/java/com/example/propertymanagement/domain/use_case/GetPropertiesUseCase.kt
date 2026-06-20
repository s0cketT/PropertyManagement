package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.repository.IPropertyRepository

class GetPropertiesUseCase(
    private val propertyRepository: IPropertyRepository,
) {
    suspend operator fun invoke(userId: String?): Resource<List<Property>, String> {
        return try {
            Resource.Success(propertyRepository.getProperties(userId))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}
