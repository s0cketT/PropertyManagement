package com.example.propertymanagement.ui.publish_screen

import com.example.propertymanagement.domain.model.ApartmentRepairType
import com.example.propertymanagement.domain.model.BalconyType
import com.example.propertymanagement.domain.model.BathroomType
import com.example.propertymanagement.domain.model.BuildingAmenity
import com.example.propertymanagement.domain.model.CeilingHeightType
import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.domain.model.CommercialRepairType
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.GasType
import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.domain.model.HouseAmenity
import com.example.propertymanagement.domain.model.HouseType
import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WaterType

data class PublishState(
    val openGallery: Boolean = false,

    val isTitleError: Boolean = false,
    val isPriceError: Boolean = false,
    val isDealTypeError: Boolean = false,
    val isPropertyTypeError: Boolean = false,
    val isAreaError: Boolean = false,
    val isLocationError: Boolean = false,

    val isPublishing: Boolean = false,

    val imageBytes: List<ByteArray> = emptyList(),

    val title: String = "",

    val description: String = "",

    val addressCountry: String = "",
    val addressRegion: String = "",
    val addressCity: String = "",
    val addressStreet: String = "",
    val addressHouse: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,

    val isAddressBottomSheetOpen: Boolean = false,
    val isMapPickerOpen: Boolean = false,
    val price: String = "",

    val area: Int? = null,
    val saleArea: Int? = null,
    val landArea: Int? = null,
    val livingArea: Int? = null,
    val kitchenArea: Int? = null,
    val floor: Int? = null,
    val floorHouse: Int? = null,
    val yearBuilt: Int? = null,

    val commercialAmenities: Set<CommercialAmenity> = emptySet(), //добавить
    val buildingAmenities: Set<BuildingAmenity> = emptySet(), //добавить
    val houseAmenities: Set<HouseAmenity> = emptySet(), //добавить

    val isWalkthroughRoom: Boolean = false,

    val currency: CurrencyType = CurrencyType.USD,
    val selectedCommercialPropertyType: CommercialPropertyType? = null,
    val selectedCommercialRepairType: CommercialRepairType? = null,
    val propertyType: PropertyType? = null,
    val dealType: DealType? = null,
    val roomsType: RoomsType? = null,
    val roomsForSaleType: RoomsType? = null,
    val roofType: RoofType? = null,
    val heatingType: HeatingType? = null,
    val waterType: WaterType? = null,
    val gasType: GasType? = null,
    val houseType: HouseType? = null,
    val parkingType: ParkingType? = null,
    val wallMaterial: WallMaterialType? = null,
    val repairType: ApartmentRepairType? = null,
    val ceilingHeight: CeilingHeightType? = null,
    val balconyType: BalconyType? = null,
    val bathroomType: BathroomType? = null
)
