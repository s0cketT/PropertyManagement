package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.FiltersPropertyDbModel
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.IntRangeFilter


fun FiltersPropertyDbModel.toDomain(): FiltersProperty {
    return FiltersProperty(
        type = type,

        price = IntRangeFilter(
            from = priceFrom,
            to = priceTo
        ),

        pricePerMeter = IntRangeFilter(
            from = pricePerMeterFrom,
            to = pricePerMeterTo
        ),

        area = IntRangeFilter(
            from = areaFrom,
            to = areaTo,
        ),

        floor = IntRangeFilter(
            from = floorFrom,
            to = floorTo,
        ),

        floorHouse = IntRangeFilter(
            from = floorHouseFrom,
            to = floorHouseTo,
        ),

        selectedCurrency = selectedCurrency,
        selectedSellerType = selectedSellerType,
        onlyWithPhotos = onlyWithPhotos,
        sortType = sortType,
        selectedDealType = selectedDealType,
        selectedCommercialPropertyType = selectedCommercialPropertyType
    )
}

fun FiltersProperty.toEntity(): FiltersPropertyDbModel {
    return FiltersPropertyDbModel(
        id = 1,
        type = type,

        priceFrom = price.from,
        priceTo = price.to,

        pricePerMeterFrom = pricePerMeter.from,
        pricePerMeterTo = pricePerMeter.to,

        areaFrom = area.from,
        areaTo = area.to,

        floorTo = floor.to,
        floorFrom = floor.from,

        floorHouseFrom = floorHouse.from,
        floorHouseTo = floorHouse.to,

        selectedCurrency = selectedCurrency,
        selectedSellerType = selectedSellerType,
        onlyWithPhotos = onlyWithPhotos,
        sortType = sortType,
        selectedDealType = selectedDealType,
        selectedCommercialPropertyType = selectedCommercialPropertyType
    )
}
