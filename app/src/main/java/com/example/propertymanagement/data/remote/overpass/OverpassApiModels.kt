package com.example.propertymanagement.data.remote.overpass

import com.google.gson.annotations.SerializedName

internal data class OverpassResponseDto(
    @SerializedName("elements")
    val elements: List<OverpassElementDto>? = null,
)

internal data class OverpassElementDto(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("center")
    val center: OverpassCenterDto? = null,
    @SerializedName("tags")
    val tags: Map<String, String>? = null,
)

internal data class OverpassCenterDto(
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
)
