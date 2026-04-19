package com.example.propertymanagement.domain.model

data class AuthUser(
    val id: String,
    val email: String?,
    val newEmail: String? = null,
)
