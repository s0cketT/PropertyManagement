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

    val commercialAmenities: Set<CommercialAmenity>,
    val commercialRepairType: CommercialRepairType?,

    val roomsForSale: RoomsType?,
    val saleArea: IntRangeFilter,

    val roomsType: RoomsType?,
    val isWalkthroughRoom: Boolean,
    val livingArea: IntRangeFilter,
    val kitchenArea: IntRangeFilter,
    val bathroomType: BathroomType?,
    val balconyType: BalconyType?,
    val ceilingHeight: CeilingHeightType?,
    val repairType: ApartmentRepairType?,
    val wallMaterial: WallMaterialType?,
    val yearBuilt: Int?,
    val buildingAmenities: Set<BuildingAmenity>,

    val houseType: HouseType?,
    val landArea: IntRangeFilter,
    val roofType: RoofType?,
    val heatingType: HeatingType?,
    val waterType: WaterType?,
    val gasType: GasType?,
    val houseAmenities: Set<HouseAmenity>,

    val parkingType: ParkingType?
)


