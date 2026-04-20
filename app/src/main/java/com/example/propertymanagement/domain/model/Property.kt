package com.example.propertymanagement.domain.model

import java.time.Instant

data class Property(
    val id: Int,
    val ownerId: String,

    /** Дата создания объявления в БД (`properties.created_at`). */
    val createdAt: Instant?,

    val type: PropertyType,
    val dealType: DealType,

    val title: String,

    val isFavorite: Boolean,

    val moderationStatus: ModerationStatus,

    val price: Double,
    val currency: CurrencyType,

    val status: PropertyStatus,

    val country: String?,
    val region: String?,
    val city: String?,
    val street: String?,
    val house: String?,
    val latitude: Double,
    val longitude: Double,

    val area: Double?,

    val rooms: RoomsType?,
    val floor: Int?,
    val totalFloors: Int?,
    val yearBuilt: Int?,

    val description: String?,

    val photos: List<String> = emptyList(),

    val details: PropertyDetails?,

    val buildingAmenities: List<BuildingAmenity> = emptyList(),
    val houseAmenities: List<HouseAmenity> = emptyList(),
    val commercialAmenities: List<CommercialAmenity> = emptyList(),
)

sealed interface PropertyDetails {

    data class Apartment(
        val isWalkthrough: Boolean?,
        val livingArea: Double?,
        val kitchenArea: Double?,
        val bathroomType: BathroomType?,
        val balconyType: BalconyType?,
        val ceilingHeight: CeilingHeightType?,
        val repairType: ApartmentRepairType?,
        val wallMaterial: WallMaterialType?
    ) : PropertyDetails

    data class Room(
        val roomsForSale: RoomsType?,
        val saleArea: Double?
    ) : PropertyDetails

    data class House(
        val houseType: HouseType?,
        val landArea: Double?,
        val floors: Int?,
        val roofType: RoofType?,
        val heatingType: HeatingType?,
        val waterType: WaterType?,
        val gasType: GasType?
    ) : PropertyDetails

    data class Garage(
        val heatingType: HeatingType?,
        val parkingType: ParkingType?
    ) : PropertyDetails

    data class Commercial(
        val repairType: CommercialRepairType?,
        val commercialType: CommercialPropertyType?
    ) : PropertyDetails
}