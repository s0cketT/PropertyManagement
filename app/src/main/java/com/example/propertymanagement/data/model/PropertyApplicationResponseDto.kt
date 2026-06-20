package com.example.propertymanagement.data.model

import com.google.gson.annotations.SerializedName

data class PropertyApplicationResponseDto(
    val id: Long,
    @field:SerializedName(value = "property_id", alternate = ["propertyId"])
    val property_id: Int,
    @field:SerializedName(value = "property_title", alternate = ["propertyTitle"])
    val property_title: String,
    @field:SerializedName(value = "application_status", alternate = ["applicationStatus"])
    val application_status: String,
    @field:SerializedName(value = "application_status_id", alternate = ["applicationStatusId"])
    val application_status_id: Int,
    @field:SerializedName(value = "created_at", alternate = ["createdAt"])
    val created_at: String? = null,
    @field:SerializedName(value = "manager_name", alternate = ["managerName"])
    val manager_name: String? = null,
    @field:SerializedName(value = "manager_email", alternate = ["managerEmail"])
    val manager_email: String? = null,
    @field:SerializedName(value = "manager_phone", alternate = ["managerPhone"])
    val manager_phone: String? = null,
)
