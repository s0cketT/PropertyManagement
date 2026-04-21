package com.example.propertymanagement.data.model

import com.google.gson.annotations.SerializedName

data class PropertyResponseDto(
    val id: Int,
    val owner_id: String?,

    /** ISO / Postgres timestamp из `properties.created_at`. */
    @field:SerializedName(value = "created_at", alternate = ["createdAt"])
    val created_at: String? = null,

    val title: String,
    val price: Double,
    val area: Double?,
    val description: String?,

    val is_favorite: Boolean,

    val currency: String,
    val type: String,
    val deal_type: String,

    @field:SerializedName(value = "moderation_status", alternate = ["moderationStatus"])
    val moderation_status: String? = null,

    @field:SerializedName(value = "moderation_status_id", alternate = ["moderationStatusId"])
    val moderation_status_id: Int? = null,

    val country: String?,
    val region: String?,
    val city: String?,
    val street: String?,
    val house: String?,

    val latitude: Double?,
    val longitude: Double?,

    val image_urls: List<String>?,

    val apartment_rooms: Int?,
    val is_walkthrough: Boolean?,
    val apartment_living_area: Double?,
    val apartment_kitchen_area: Double?,
    val apartment_floor: Int?,
    val apartment_total_floors: Int?,
    val apartment_ceiling_height: Double?,
    val apartment_year_built: Int?,
    val bathroom_type: String?,
    val balcony_type: String?,
    val apartment_repair_type: String?,
    val apartment_wall_material: String?,

    val room_total: Int?,
    val room_for_sale: Int?,
    val sale_area: Double?,
    val room_kitchen_area: Double?,
    val room_floor: Int?,
    val room_total_floors: Int?,
    val room_ceiling_height: Double?,
    val room_bathroom_type: String?,
    val room_repair_type: String?,
    val room_wall_material: String?,

    val house_rooms: Int?,
    val house_type: String?,
    val land_area: Double?,
    val house_living_area: Double?,
    val house_kitchen_area: Double?,
    val house_floors: Int?,
    val house_ceiling_height: Double?,
    val house_year_built: Int?,
    val house_wall_material: String?,
    val roof_type: String?,
    val house_heating_type: String?,
    val water_type: String?,
    val gas_type: String?,

    val commercial_rooms: Int?,
    val commercial_type: String?,
    val commercial_floor: Int?,
    val commercial_total_floors: Int?,
    val commercial_repair_type: String?,

    val garage_heating_type: String?,
    val parking_type: String?,

    val building_amenities: List<String>?,
    val house_amenities: List<String>?,
    val commercial_amenities: List<String>?
)