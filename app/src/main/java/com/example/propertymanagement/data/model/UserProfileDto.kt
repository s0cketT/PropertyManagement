package com.example.propertymanagement.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: String,
    val name: String,
    val email: String,
    val avatar_url: String?,
    val seller_types: SellerTypeDto
)

@Serializable
data class SellerTypeDto(
    val name: String
)
