package com.example.propertymanagement.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserEmailPayload(
    val email: String
)
