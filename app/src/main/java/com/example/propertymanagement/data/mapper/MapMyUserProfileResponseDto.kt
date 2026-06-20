package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.MyUserProfileResponseDto
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.UserProfile

fun MyUserProfileResponseDto.toUserProfileDomain(): UserProfile {
    return UserProfile(
        id = id,
        name = name,
        email = email,
        phone = phone,
        avatarUrl = avatarUrl,
        sellerType = sellerTypeName?.fromDb<SellerType>() ?: SellerType.OWNER,
        appRating = appRating,
    )
}
