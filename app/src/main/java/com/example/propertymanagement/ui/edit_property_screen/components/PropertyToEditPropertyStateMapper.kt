package com.example.propertymanagement.ui.edit_property_screen.components

import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetails
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyState

fun Property.toEditPropertyState(): EditPropertyState {
    val base = EditPropertyState(
        editingPropertyId = id,
        existingImageUrls = photos,
        hadRemotePhotosWhenLoaded = photos.isNotEmpty(),
        title = title,
        description = description.orEmpty(),
        addressCountry = country.orEmpty(),
        addressRegion = region.orEmpty(),
        addressCity = city.orEmpty(),
        addressStreet = street.orEmpty(),
        addressHouse = house.orEmpty(),
        latitude = latitude,
        longitude = longitude,
        price = formatPriceForEdit(price),
        currency = currency,
        propertyType = type,
        dealType = dealType,
        area = area?.toInt()?.takeIf { it > 0 },
        floor = floor,
        floorHouse = totalFloors,
        yearBuilt = yearBuilt,
        roomsType = rooms,
        commercialAmenities = commercialAmenities.toSet(),
        buildingAmenities = buildingAmenities.toSet(),
        houseAmenities = houseAmenities.toSet(),
    )

    return when (val d = details) {
        is PropertyDetails.Apartment -> base.copy(
            livingArea = d.livingArea?.toInt(),
            kitchenArea = d.kitchenArea?.toInt(),
            balconyType = d.balconyType,
            bathroomType = d.bathroomType,
            ceilingHeight = d.ceilingHeight,
            repairType = d.repairType,
            wallMaterial = d.wallMaterial,
            windowViews = d.windowViews,
            isWalkthroughRoom = d.isWalkthrough == true,
        )

        is PropertyDetails.Room -> base.copy(
            roomsForSaleType = d.roomsForSale,
            saleArea = d.saleArea?.toInt(),
            windowViews = d.windowViews,
        )

        is PropertyDetails.House -> base.copy(
            houseType = d.houseType,
            landArea = d.landArea?.toInt(),
            livingArea = area?.toInt()?.takeIf { it > 0 },
            kitchenArea = null,
            ceilingHeight = null,
            wallMaterial = null,
            floorHouse = d.floors ?: base.floorHouse,
            roofType = d.roofType,
            heatingType = d.heatingType,
            waterType = d.waterType,
            gasType = d.gasType,
        )

        is PropertyDetails.Garage -> base.copy(
            heatingType = d.heatingType,
            parkingType = d.parkingType,
        )

        is PropertyDetails.Commercial -> base.copy(
            selectedCommercialPropertyType = d.commercialType,
            selectedCommercialRepairType = d.repairType,
        )

        null -> base
    }
}

private fun formatPriceForEdit(price: Double): String {
    val asLong = price.toLong()
    return if (asLong.toDouble() == price) {
        asLong.toString()
    } else {
        price.toString()
    }
}
