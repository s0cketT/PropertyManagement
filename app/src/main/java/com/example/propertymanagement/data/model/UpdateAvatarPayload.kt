package com.example.propertymanagement.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAvatarPayload(
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)
