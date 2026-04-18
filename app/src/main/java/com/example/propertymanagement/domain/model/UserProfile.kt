package com.example.propertymanagement.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val avatarUrl: String?,
    val sellerType: SellerType
)
