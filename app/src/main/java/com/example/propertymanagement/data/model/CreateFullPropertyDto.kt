package com.example.propertymanagement.data.model

data class CreateFullPropertyDto(
    val p_title: String,
    val p_description: String? = null,
    val p_price: Double,
    val p_currency: String,
    val p_type: String,
    val p_deal_type: String,
    val p_owner_id: String,

    val p_country: String,
    val p_region: String? = null,
    val p_city: String,
    val p_street: String? = null,
    val p_house: String? = null,
    val p_lat: Double,
    val p_lng: Double,

    val p_rooms: Int? = null,
    val p_floor: Int? = null,
    val p_total_floors: Int? = null,
    val p_year_built: Int? = null,

    val p_area: Double? = null,

    val p_commercial_amenities: List<String> = emptyList(),
    val p_building_amenities: List<String> = emptyList(),
    val p_house_amenities: List<String> = emptyList(),

    val p_commercial_type: String? = null,
    val p_commercial_repair_type: String? = null,

    val p_is_walkthrough: Boolean? = null,
    val p_living_area: Double? = null,
    val p_kitchen_area: Double? = null,
    val p_bathroom_type: String? = null,
    val p_balcony_type: String? = null,
    val p_ceiling_height: Double? = null,
    val p_apartment_repair_type: String? = null,
    val p_wall_material: String? = null,

    val p_rooms_for_sale: Int? = null,
    val p_sale_area: Double? = null,

    val p_house_type: String? = null,
    val p_land_area: Double? = null,
    val p_floors: Int? = null,
    val p_roof_type: String? = null,
    val p_heating_type: String? = null,
    val p_water_type: String? = null,
    val p_gas_type: String? = null,

    val p_parking_type: String? = null
)