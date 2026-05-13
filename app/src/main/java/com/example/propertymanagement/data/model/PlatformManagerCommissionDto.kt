package com.example.propertymanagement.data.model

import com.google.gson.annotations.SerializedName

data class PlatformManagerCommissionDto(
    val id: Int? = null,
    @SerializedName(value = "commission_percent", alternate = ["commissionPercent"])
    val commission_percent: Double?,
)
