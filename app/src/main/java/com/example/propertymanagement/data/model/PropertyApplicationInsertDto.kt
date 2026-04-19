package com.example.propertymanagement.data.model

data class PropertyApplicationInsertDto(
    val property_id: Int,
    val applicant_user_id: String,
    val comment: String? = null
)
