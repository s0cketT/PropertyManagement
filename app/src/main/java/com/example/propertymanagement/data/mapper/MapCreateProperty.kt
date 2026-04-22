package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.CreateFullPropertyDto
import com.example.propertymanagement.data.model.UpdateFullPropertyDto
import com.example.propertymanagement.domain.model.CreateProperty

fun CreateProperty.toFullDto(): CreateFullPropertyDto {
    return CreateFullPropertyDto(
        p_title = title,
        p_description = description?.trim()?.takeIf { it.isNotEmpty() },
        p_price = price,
        p_currency = currency.toDbValue(),
        p_type = type.toDbValue(),
        p_deal_type = dealType.toDbValue(),
        p_owner_id = ownerId,

        p_country = country,
        p_region = region.takeIf { it.isNotBlank() },
        p_city = city,
        p_street = street.takeIf { it.isNotBlank() },
        p_house = house.takeIf { it.isNotBlank() },
        p_lat = latitude,
        p_lng = longitude,

        p_rooms = rooms?.toInt(),
        p_floor = floor,
        p_total_floors = totalFloors,
        p_year_built = yearBuilt,

        p_area = area,

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

fun CreateProperty.toUpdateDto(propertyId: Int): UpdateFullPropertyDto {
    val f = toFullDto()
    return UpdateFullPropertyDto(
        p_property_id = propertyId,
        p_title = f.p_title,
        p_description = f.p_description,
        p_price = f.p_price,
        p_currency = f.p_currency,
        p_type = f.p_type,
        p_deal_type = f.p_deal_type,

        p_country = f.p_country,
        p_region = f.p_region,
        p_city = f.p_city,
        p_street = f.p_street,
        p_house = f.p_house,
        p_lat = f.p_lat,
        p_lng = f.p_lng,

        p_rooms = f.p_rooms,
        p_floor = f.p_floor,
        p_total_floors = f.p_total_floors,
        p_year_built = f.p_year_built,

        p_area = f.p_area,

        p_commercial_amenities = f.p_commercial_amenities,
        p_building_amenities = f.p_building_amenities,
        p_house_amenities = f.p_house_amenities,

        p_commercial_type = f.p_commercial_type,
        p_commercial_repair_type = f.p_commercial_repair_type,

        p_is_walkthrough = f.p_is_walkthrough,
        p_living_area = f.p_living_area,
        p_kitchen_area = f.p_kitchen_area,
        p_bathroom_type = f.p_bathroom_type,
        p_balcony_type = f.p_balcony_type,
        p_ceiling_height = f.p_ceiling_height,
        p_apartment_repair_type = f.p_apartment_repair_type,
        p_wall_material = f.p_wall_material,

        p_rooms_for_sale = f.p_rooms_for_sale,
        p_sale_area = f.p_sale_area,

        p_house_type = f.p_house_type,
        p_land_area = f.p_land_area,
        p_floors = f.p_floors,
        p_roof_type = f.p_roof_type,
        p_heating_type = f.p_heating_type,
        p_water_type = f.p_water_type,
        p_gas_type = f.p_gas_type,

        p_parking_type = f.p_parking_type,
    )
}