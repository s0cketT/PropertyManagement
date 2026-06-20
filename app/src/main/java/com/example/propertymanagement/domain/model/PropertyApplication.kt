package com.example.propertymanagement.domain.model

import java.time.Instant

data class PropertyApplication(
    val id: Long,
    val propertyId: Int,
    val propertyTitle: String,
    val status: ApplicationStatus,
    val statusId: Int,
    val createdAt: Instant?,
    val managerName: String?,
    val managerEmail: String?,
    val managerPhone: String?,
) {
    val hasManagerContact: Boolean
        get() = !managerName.isNullOrBlank() ||
            !managerEmail.isNullOrBlank() ||
            !managerPhone.isNullOrBlank()
}
