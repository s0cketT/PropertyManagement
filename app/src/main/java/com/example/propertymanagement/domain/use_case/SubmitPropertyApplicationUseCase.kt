package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.IPropertyRepository

class SubmitPropertyApplicationUseCase(
    private val propertyRepository: IPropertyRepository
) {
    suspend operator fun invoke(
        propertyId: Int,
        applicantUserId: String,
        comment: String?
    ) {
        val trimmed = comment?.trim()?.takeIf { it.isNotEmpty() }
        propertyRepository.submitPropertyApplication(
            propertyId = propertyId,
            applicantUserId = applicantUserId,
            comment = trimmed
        )
    }
}
