package com.example.propertymanagement.data.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.EncodeDefault.Mode.ALWAYS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UpdateAvatarPayload(
    @SerialName("avatar_url")
    @EncodeDefault(ALWAYS)
    val avatarUrl: String? = null,
)
