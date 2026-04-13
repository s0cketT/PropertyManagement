package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.CreateFullPropertyDto
import com.example.propertymanagement.domain.model.CreateProperty

fun CreateProperty.toFullDto(): CreateFullPropertyDto {
    return CreateFullPropertyDto(
        p_title = title,
        p_price = price,
        p_currency = currency.toDbValue(),
        p_type = type.toDbValue(),
        p_deal_type = dealType.toDbValue(),
        p_owner_id = ownerId,

        p_country = country,
        p_city = city,
        p_lat = latitude,
        p_lng = longitude,

        p_rooms = rooms?.toInt(),
        p_floor = floor,
        p_total_floors = totalFloors,
        p_year_built = yearBuilt,

        // AMENITIES
        p_commercial_amenities = commercialAmenities.map { it.toDbValue() },
        p_building_amenities = buildingAmenities.map { it.toDbValue() },
        p_house_amenities = houseAmenities.map { it.toDbValue() },

        p_commercial_type = commercialType?.toDbValue(),
        p_commercial_repair_type = commercialRepairType?.toDbValue(),

        p_is_walkthrough = isWalkthrough,
        p_living_area = livingArea,
        p_kitchen_area = kitchenArea,
        p_bathroom_type = bathroomType?.toDbValue(),
        p_balcony_type = balconyType?.toDbValue(),
        p_ceiling_height = ceilingHeight?.toDbValue(),
        p_apartment_repair_type = repairType?.toDbValue(),
        p_wall_material = wallMaterial?.toDbValue(),

        p_rooms_for_sale = roomsForSale?.toInt(),
        p_sale_area = saleArea,

        p_house_type = houseType?.toDbValue(),
        p_land_area = landArea,
        p_floors = floors,
        p_roof_type = roofType?.toDbValue(),
        p_heating_type = heatingType?.toDbValue(),
        p_water_type = waterType?.toDbValue(),
        p_gas_type = gasType?.toDbValue(),

        p_parking_type = parkingType?.toDbValue()
    )
}