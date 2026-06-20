package com.example.propertymanagement.data.model

import com.google.gson.annotations.SerializedName

data class MyUserProfileResponseDto(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    @field:SerializedName(value = "avatar_url", alternate = ["avatarUrl"])
    val avatarUrl: String?,
    @field:SerializedName(value = "app_rating", alternate = ["appRating"])
    val appRating: Int?,
    @field:SerializedName(value = "seller_type_name", alternate = ["sellerTypeName"])
    val sellerTypeName: String?,
)
