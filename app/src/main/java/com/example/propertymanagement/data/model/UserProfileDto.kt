package com.example.propertymanagement.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val avatar_url: String?,
    val app_rating: Int? = null,
    val seller_types: SellerTypeDto
)

@Serializable
data class SellerTypeDto(
    val name: String
)
