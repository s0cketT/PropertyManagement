package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.PropertyApplication
import com.example.propertymanagement.domain.repository.IPropertyRepository

class GetMyPropertyApplicationsUseCase(
    private val propertyRepository: IPropertyRepository,
) {
    suspend operator fun invoke(userId: String): Resource<List<PropertyApplication>, String> {
        return try {
            Resource.Success(propertyRepository.getMyPropertyApplications(userId))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}
