package com.example.propertymanagement.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val email: String
)
