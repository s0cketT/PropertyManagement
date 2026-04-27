package com.example.propertymanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.propertymanagement.domain.model.ApartmentRepairType
import com.example.propertymanagement.domain.model.BalconyType
import com.example.propertymanagement.domain.model.BathroomType
import com.example.propertymanagement.domain.model.CeilingHeightType
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.domain.model.CommercialRepairType
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.GasType
import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.domain.model.HouseType
import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WaterType

@Entity(tableName = "selected_property_marker")
data class FiltersPropertyDbModel(
    @PrimaryKey
    val id: Int,

    val type: PropertyType?,
    val selectedRegionId: Long?,
    val selectedRegionName: String?,
    val selectedCityIds: String,
    val selectedCityNames: String,
    val selectedLocationLat: Double?,
    val selectedLocationLng: Double?,

    val priceFrom: Int?,
    val priceTo: Int?,
    val pricePerMeterFrom: Int?,
    val pricePerMeterTo: Int?,

    val areaFrom: Int?,
    val areaTo: Int?,
    val floorFrom: Int?,
    val floorTo: Int?,
    val floorHouseFrom: Int?,
    val floorHouseTo: Int?,
    val separateRoomsFrom: Int?,
    val separateRoomsTo: Int?,

    val selectedCurrency: CurrencyType,

    val selectedSellerType: SellerType?,
    val onlyWithPhotos: Boolean,
    val sortType: SortType,
    val selectedDealType: DealType?,

    val selectedCommercialPropertyType: CommercialPropertyType?,
    val commercialAmenities: String,
    val commercialRepairType: CommercialRepairType?,

    val roomsForSale: RoomsType?,
    val saleAreaFrom: Int?,
    val saleAreaTo: Int?,

    // Apartment
    val roomsType: RoomsType?,
    val isWalkthroughRoom: Boolean,
    val livingAreaFrom: Int?,
    val livingAreaTo: Int?,
    val kitchenAreaFrom: Int?,
    val kitchenAreaTo: Int?,
    val bathroomType: BathroomType?,
    val balconyType: BalconyType?,
    val ceilingHeight: CeilingHeightType?,
    val repairType: ApartmentRepairType?,
    val wallMaterial: WallMaterialType?,
    val yearBuilt: Int?,
    val buildingAmenities: String,

    // House
    val houseType: HouseType?,
    val landAreaFrom: Int?,
    val landAreaTo: Int?,
    val roofType: RoofType?,
    val heatingType: HeatingType?,
    val waterType: WaterType?,
    val gasType: GasType?,
    val houseAmenities: String,

    val parkingType: ParkingType?
)