package com.example.propertymanagement.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserAppRatingPayload(
    @SerialName("app_rating")
    val appRating: Int
)
