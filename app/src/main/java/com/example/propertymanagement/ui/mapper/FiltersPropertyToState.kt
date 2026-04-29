package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.StringRangeFilter
import com.example.propertymanagement.ui.filters_screen.FiltersState

fun FiltersProperty.toState(): FiltersState {
    return FiltersState(
        selectedPropertyType = type,
        selectedRegionId = selectedRegionId,
        selectedRegionName = selectedRegionName,
        selectedCityIds = selectedCityIds,
        selectedCityNames = selectedCityNames,
        selectedLocationLat = selectedLocationLat,
        selectedLocationLng = selectedLocationLng,

        price = StringRangeFilter(
            from = price.from?.toString(),
            to = price.to?.toString()
        ),

        pricePerMeter = StringRangeFilter(
            from = pricePerMeter.from?.toString(),
            to = pricePerMeter.to?.toString()
        ),

        area = area,
        floor = floor,
        floorHouse = floorHouse,
        separateRooms = separateRooms,

        selectedCurrency = selectedCurrency,

        sellerType = selectedSellerType,
        onlyWithPhotos = onlyWithPhotos,
        sortType = sortType,
        dealType = selectedDealType,
        commercialPropertyType = selectedCommercialPropertyType,

        commercialAmenities = commercialAmenities,
        commercialRepairType = commercialRepairType,

        roomsForSale = roomsForSale,
        saleArea = saleArea,

        roomsType = roomsType,
        isWalkthroughRoom = isWalkthroughRoom,
        livingArea = livingArea,
        kitchenArea = kitchenArea,
        bathroomType = bathroomType,
        balconyType = balconyType,
        ceilingHeight = ceilingHeight,
        repairType = repairType,
        wallMaterial = wallMaterial,
        windowViews = windowViews,
        yearBuilt = yearBuilt,
        buildingAmenities = buildingAmenities,

        houseType = houseType,
        landArea = landArea,
        roofType = roofType,
        heatingType = heatingType,
        waterType = waterType,
        gasType = gasType,
        houseAmenities = houseAmenities,

        parkingType = parkingType
    )
}