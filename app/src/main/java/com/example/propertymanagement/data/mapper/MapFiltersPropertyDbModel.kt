package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.FiltersPropertyDbModel
import com.example.propertymanagement.domain.model.BuildingAmenity
import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.HouseAmenity
import com.example.propertymanagement.domain.model.IntRangeFilter

fun FiltersPropertyDbModel.toDomain(): FiltersProperty {
    return FiltersProperty(
        type = type,
        selectedRegionId = selectedRegionId,
        selectedRegionName = selectedRegionName,
        selectedCityIds = selectedCityIds.toLongSet(),
        selectedCityNames = selectedCityNames.toStringSet(),
        selectedLocationLat = selectedLocationLat,
        selectedLocationLng = selectedLocationLng,

        price = IntRangeFilter(priceFrom, priceTo),
        pricePerMeter = IntRangeFilter(pricePerMeterFrom, pricePerMeterTo),

        area = IntRangeFilter(areaFrom, areaTo),
        floor = IntRangeFilter(floorFrom, floorTo),
        floorHouse = IntRangeFilter(floorHouseFrom, floorHouseTo),
        separateRooms = IntRangeFilter(separateRoomsFrom, separateRoomsTo),

        selectedCurrency = selectedCurrency,
        selectedSellerType = selectedSellerType,
        onlyWithPhotos = onlyWithPhotos,
        sortType = sortType,
        selectedDealType = selectedDealType,
        selectedCommercialPropertyType = selectedCommercialPropertyType,

        commercialAmenities = commercialAmenities.toCommercialAmenitySet(),
        commercialRepairType = commercialRepairType,

        roomsForSale = roomsForSale,
        saleArea = IntRangeFilter(saleAreaFrom, saleAreaTo),

        roomsType = roomsType,
        isWalkthroughRoom = isWalkthroughRoom,
        livingArea = IntRangeFilter(livingAreaFrom, livingAreaTo),
        kitchenArea = IntRangeFilter(kitchenAreaFrom, kitchenAreaTo),
        bathroomType = bathroomType,
        balconyType = balconyType,
        ceilingHeight = ceilingHeight,
        repairType = repairType,
        wallMaterial = wallMaterial,
        yearBuilt = yearBuilt,
        buildingAmenities = buildingAmenities.toBuildingAmenitySet(),

        houseType = houseType,
        landArea = IntRangeFilter(landAreaFrom, landAreaTo),
        roofType = roofType,
        heatingType = heatingType,
        waterType = waterType,
        gasType = gasType,
        houseAmenities = houseAmenities.toHouseAmenitySet(),

        parkingType = parkingType
    )
}

fun FiltersProperty.toEntity(): FiltersPropertyDbModel {
    return FiltersPropertyDbModel(
        id = 1,
        type = type,
        selectedRegionId = selectedRegionId,
        selectedRegionName = selectedRegionName,
        selectedCityIds = selectedCityIds.toDbLongSet(),
        selectedCityNames = selectedCityNames.toDbStringSet(),
        selectedLocationLat = selectedLocationLat,
        selectedLocationLng = selectedLocationLng,

        priceFrom = price.from,
        priceTo = price.to,
        pricePerMeterFrom = pricePerMeter.from,
        pricePerMeterTo = pricePerMeter.to,

        areaFrom = area.from,
        areaTo = area.to,
        floorFrom = floor.from,
        floorTo = floor.to,
        floorHouseFrom = floorHouse.from,
        floorHouseTo = floorHouse.to,
        separateRoomsFrom = separateRooms.from,
        separateRoomsTo = separateRooms.to,

        selectedCurrency = selectedCurrency,
        selectedSellerType = selectedSellerType,
        onlyWithPhotos = onlyWithPhotos,
        sortType = sortType,
        selectedDealType = selectedDealType,
        selectedCommercialPropertyType = selectedCommercialPropertyType,

        commercialAmenities = commercialAmenities.toCommercialDb(),
        commercialRepairType = commercialRepairType,

        roomsForSale = roomsForSale,
        saleAreaFrom = saleArea.from,
        saleAreaTo = saleArea.to,

        roomsType = roomsType,
        isWalkthroughRoom = isWalkthroughRoom,
        livingAreaFrom = livingArea.from,
        livingAreaTo = livingArea.to,
        kitchenAreaFrom = kitchenArea.from,
        kitchenAreaTo = kitchenArea.to,
        bathroomType = bathroomType,
        balconyType = balconyType,
        ceilingHeight = ceilingHeight,
        repairType = repairType,
        wallMaterial = wallMaterial,
        yearBuilt = yearBuilt,
        buildingAmenities = buildingAmenities.toBuildingDb(),

        houseType = houseType,
        landAreaFrom = landArea.from,
        landAreaTo = landArea.to,
        roofType = roofType,
        heatingType = heatingType,
        waterType = waterType,
        gasType = gasType,
        houseAmenities = houseAmenities.toHouseDb(),

        parkingType = parkingType
    )
}

fun String.toCommercialAmenitySet(): Set<CommercialAmenity> {
    if (this.isBlank()) return emptySet()

    return this.split(",")
        .mapNotNull {
            runCatching { CommercialAmenity.valueOf(it.trim()) }.getOrNull()
        }
        .toSet()
}

fun Set<CommercialAmenity>.toCommercialDb(): String {
    return this.joinToString(",") { it.name }
}

fun String.toBuildingAmenitySet(): Set<BuildingAmenity> {
    if (this.isBlank()) return emptySet()

    return this.split(",")
        .mapNotNull {
            runCatching { BuildingAmenity.valueOf(it.trim()) }.getOrNull()
        }
        .toSet()
}

fun Set<BuildingAmenity>.toBuildingDb(): String {
    return this.joinToString(",") { it.name }
}

fun String.toHouseAmenitySet(): Set<HouseAmenity> {
    if (this.isBlank()) return emptySet()

    return this.split(",")
        .mapNotNull {
            runCatching { HouseAmenity.valueOf(it.trim()) }.getOrNull()
        }
        .toSet()
}

fun Set<HouseAmenity>.toHouseDb(): String {
    return this.joinToString(",") { it.name }
}

private fun String.toLongSet(): Set<Long> {
    if (isBlank()) {
        return emptySet()
    }

    return split(",")
        .mapNotNull { value -> value.trim().toLongOrNull() }
        .toSet()
}

private fun Set<Long>.toDbLongSet(): String {
    return joinToString(",")
}

private fun String.toStringSet(): Set<String> {
    if (isBlank()) {
        return emptySet()
    }

    return split(";;")
        .map { value -> value.trim() }
        .filter { value -> value.isNotEmpty() }
        .toSet()
}

private fun Set<String>.toDbStringSet(): String {
    return joinToString(";;")
}