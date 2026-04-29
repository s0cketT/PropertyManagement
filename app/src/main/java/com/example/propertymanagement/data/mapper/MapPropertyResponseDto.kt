package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.PropertyResponseDto
import com.example.propertymanagement.domain.model.ApartmentRepairType
import com.example.propertymanagement.domain.model.BalconyType
import com.example.propertymanagement.domain.model.BathroomType
import com.example.propertymanagement.domain.model.BuildingAmenity
import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.domain.model.CommercialRepairType
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.GasType
import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.domain.model.HouseAmenity
import com.example.propertymanagement.domain.model.ModerationStatus
import com.example.propertymanagement.domain.model.HouseType
import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetails
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WindowViewType
import com.example.propertymanagement.domain.model.WaterType
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private fun String?.parseCreatedAt(): Instant? {
    if (isNullOrBlank()) return null
    val s = trim()
    runCatching { Instant.parse(s) }.getOrNull()?.let { return it }
    runCatching {
        OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant()
    }.getOrNull()?.let { return it }
    runCatching {
        OffsetDateTime.parse(s, DateTimeFormatter.ISO_ZONED_DATE_TIME).toInstant()
    }.getOrNull()?.let { return it }
    val normalized = if ('T' in s) s else s.replaceFirst(" ", "T")
    return runCatching {
        LocalDateTime.parse(normalized, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toInstant(ZoneOffset.UTC)
    }.getOrNull()
}

fun PropertyResponseDto.toDomain(): Property {

    val dealType = deal_type.fromDb<DealType>() ?: DealType.BUY
    val propertyType = type.fromDb<PropertyType>() ?: PropertyType.APARTMENT
    val currencyType = currency.fromDb<CurrencyType>() ?: CurrencyType.USD

    val details: PropertyDetails? = when {
        apartment_rooms != null -> PropertyDetails.Apartment(
            isWalkthrough = is_walkthrough,
            livingArea = apartment_living_area,
            kitchenArea = apartment_kitchen_area,
            bathroomType = bathroom_type.fromDb<BathroomType>(),
            balconyType = balcony_type.fromDb<BalconyType>(),
            ceilingHeight = apartment_ceiling_height?.toCeilingHeightType(),
            repairType = apartment_repair_type.fromDb<ApartmentRepairType>(),
            wallMaterial = apartment_wall_material.fromDb<WallMaterialType>(),
            windowViews = window_views.orEmpty()
                .mapNotNull { it.fromDb<WindowViewType>() }
                .toSet()
        )

        room_total != null -> PropertyDetails.Room(
            roomsForSale = room_for_sale?.toRoomsType(),
            saleArea = sale_area,
            windowViews = window_views.orEmpty()
                .mapNotNull { it.fromDb<WindowViewType>() }
                .toSet()
        )

        house_rooms != null -> PropertyDetails.House(
            houseType = house_type.fromDb<HouseType>(),
            landArea = land_area,
            floors = house_floors,
            roofType = roof_type.fromDb<RoofType>(),
            heatingType = house_heating_type.fromDb<HeatingType>(),
            waterType = water_type.fromDb<WaterType>(),
            gasType = gas_type.fromDb<GasType>()
        )

        garage_heating_type != null || parking_type != null -> PropertyDetails.Garage(
            heatingType = garage_heating_type.fromDb<HeatingType>(),
            parkingType = parking_type.fromDb<ParkingType>()
        )

        commercial_type != null -> PropertyDetails.Commercial(
            repairType = commercial_repair_type.fromDb<CommercialRepairType>(),
            commercialType = commercial_type.fromDb<CommercialPropertyType>()
        )

        else -> null
    }

    return Property(
        id = id,
        ownerId = owner_id.orEmpty(),
        createdAt = created_at.parseCreatedAt(),

        type = propertyType,
        dealType = dealType,
        title = title,
        price = price,
        currency = currencyType,

        isFavorite = is_favorite,

        moderationStatus = ModerationStatus.fromDb(moderation_status),
        adminComment = admin_comment,

        country = country,
        region = region,
        city = city,
        street = street,
        house = house,
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0,

        area = area,

        rooms = apartment_rooms?.toRoomsType()
            ?: room_total?.toRoomsType()
            ?: house_rooms?.toRoomsType()
            ?: commercial_rooms?.toRoomsType(),

        floor = apartment_floor
            ?: room_floor
            ?: commercial_floor,

        totalFloors = apartment_total_floors
            ?: room_total_floors
            ?: commercial_total_floors,

        yearBuilt = apartment_year_built
            ?: house_year_built,

        description = description,

        photos = image_urls ?: emptyList(),

        details = details,
        status = dealType.toPropertyStatus(),

        buildingAmenities = building_amenities
            ?.mapNotNull { it.fromDb<BuildingAmenity>() }
            ?: emptyList(),

        houseAmenities = house_amenities
            ?.mapNotNull { it.fromDb<HouseAmenity>() }
            ?: emptyList(),

        commercialAmenities = commercial_amenities
            ?.mapNotNull { it.fromDb<CommercialAmenity>() }
            ?: emptyList()
    )
}