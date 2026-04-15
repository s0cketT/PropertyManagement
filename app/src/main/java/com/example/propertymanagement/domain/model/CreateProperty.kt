package com.example.propertymanagement.domain.model

data class CreateProperty(
    val ownerId: String,

    val type: PropertyType,
    val price: Double,
    val currency: CurrencyType,
    val dealType: DealType,

    val title: String,

    val country: String,
    val region: String,
    val city: String,
    val street: String,
    val house: String,
    val latitude: Double,
    val longitude: Double,

    val area: Double?,

    val rooms: RoomsType?,
    val floor: Int?,
    val totalFloors: Int?,
    val yearBuilt: Int?,

    val commercialAmenities: Set<CommercialAmenity> = emptySet(),
    val buildingAmenities: Set<BuildingAmenity> = emptySet(),
    val houseAmenities: Set<HouseAmenity> = emptySet(),

    val commercialType: CommercialPropertyType? = null,
    val commercialRepairType: CommercialRepairType? = null,

    val isWalkthrough: Boolean? = null,
    val livingArea: Double? = null,
    val kitchenArea: Double? = null,
    val bathroomType: BathroomType? = null,
    val balconyType: BalconyType? = null,
    val ceilingHeight: CeilingHeightType? = null,
    val repairType: ApartmentRepairType? = null,
    val wallMaterial: WallMaterialType? = null,

    val roomsForSale: RoomsType? = null,
    val saleArea: Double? = null,

    val houseType: HouseType? = null,
    val landArea: Double? = null,
    val floors: Int? = null,
    val roofType: RoofType? = null,
    val heatingType: HeatingType? = null,
    val waterType: WaterType? = null,
    val gasType: GasType? = null,

    val parkingType: ParkingType? = null
)
