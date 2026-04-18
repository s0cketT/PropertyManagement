package com.example.propertymanagement.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfilePayload(
    val name: String,
    val phone: String,
    @SerialName("seller_type_id")
    val sellerTypeId: Int
)
