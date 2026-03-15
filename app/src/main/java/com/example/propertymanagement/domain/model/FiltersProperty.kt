package com.example.propertymanagement.domain.model

data class FiltersProperty(
    val type: PropertyType?,

    val price: IntRangeFilter,
    val pricePerMeter: IntRangeFilter,

    val area: IntRangeFilter,
    val floor: IntRangeFilter,
    val floorHouse: IntRangeFilter,
    val separateRooms: IntRangeFilter,

    val selectedCurrency: CurrencyType,

    val selectedSellerType: SellerType?,
    val onlyWithPhotos: Boolean,
    val sortType: SortType,
    val selectedDealType: DealType?,
    val selectedCommercialPropertyType: CommercialPropertyType?,

    val commercialAmenities: Set<CommercialAmenity>
)



