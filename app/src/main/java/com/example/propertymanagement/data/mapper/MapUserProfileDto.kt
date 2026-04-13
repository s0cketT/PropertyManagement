package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.UserProfileDto
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.UserProfile

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        name = name,
        email = email,
        avatarUrl = avatar_url,
        sellerType = seller_types.name.fromDb<SellerType>()!!
    )
}