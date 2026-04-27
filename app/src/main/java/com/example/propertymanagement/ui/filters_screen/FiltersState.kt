package com.example.propertymanagement.ui.filters_screen


import com.example.propertymanagement.domain.model.ApartmentRepairType
import com.example.propertymanagement.domain.model.BalconyType
import com.example.propertymanagement.domain.model.BathroomType
import com.example.propertymanagement.domain.model.BuildingAmenity
import com.example.propertymanagement.domain.model.CeilingHeightType
import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.domain.model.CommercialRepairType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.GasType
import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.domain.model.HouseAmenity
import com.example.propertymanagement.domain.model.HouseType
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.domain.model.StringRangeFilter
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WaterType
import com.example.propertymanagement.ui.extensions.isRangeValid

data class FiltersState(
    val selectedPropertyType:  PropertyType? = null,
    val selectedRegionId: Long? = null,
    val selectedRegionName: String? = null,
    val selectedCityIds: Set<Long> = emptySet(),
    val selectedCityNames: Set<String> = emptySet(),
    val selectedLocationLat: Double? = null,
    val selectedLocationLng: Double? = null,
    val matchedPropertiesCount: Int = 0,

    val selectedCurrency: CurrencyType = CurrencyType.USD,

    val price: StringRangeFilter = StringRangeFilter(),
    val pricePerMeter: StringRangeFilter = StringRangeFilter(),

    val sellerType: SellerType? = null,
    val onlyWithPhotos: Boolean = false,
    val sortType: SortType = SortType.NEWEST,

    val dealType: DealType? = null,

    val area: IntRangeFilter = IntRangeFilter(),
    val floor: IntRangeFilter = IntRangeFilter(),
    val floorHouse: IntRangeFilter = IntRangeFilter(),
    val separateRooms: IntRangeFilter = IntRangeFilter(),

    val commercialPropertyType: CommercialPropertyType? = null,
    val commercialAmenities: Set<CommercialAmenity> = emptySet(),
    val commercialRepairType: CommercialRepairType? = null,

    val roomsForSale: RoomsType? = null,
    val saleArea: IntRangeFilter = IntRangeFilter(),

    // Apartment
    val roomsType: RoomsType? = null,
    val isWalkthroughRoom: Boolean = false,
    val livingArea: IntRangeFilter = IntRangeFilter(),
    val kitchenArea: IntRangeFilter = IntRangeFilter(),
    val bathroomType: BathroomType? = null,
    val balconyType: BalconyType? = null,
    val ceilingHeight: CeilingHeightType? = null,
    val repairType: ApartmentRepairType? = null,
    val wallMaterial: WallMaterialType? = null,
    val yearBuilt: Int? = null,
    val buildingAmenities: Set<BuildingAmenity> = emptySet(),

    val houseType: HouseType? = null,
    val landArea: IntRangeFilter = IntRangeFilter(),
    val roofType: RoofType? = null,
    val heatingType: HeatingType? = null,
    val waterType: WaterType? = null,
    val gasType: GasType? = null,
    val houseAmenities: Set<HouseAmenity> = emptySet(),

    val parkingType: ParkingType? = null,
) {
    val isFiltersValid: Boolean
        get() = isRangeValid(price.from?.toIntOrNull(), price.to?.toIntOrNull()) &&
                isRangeValid(pricePerMeter.from?.toIntOrNull(), pricePerMeter.to?.toIntOrNull()) &&
                isRangeValid(area.from, area.to) &&
                isRangeValid(floor.from, floor.to) &&
                isRangeValid(floorHouse.from, floorHouse.to) &&
                isRangeValid(separateRooms.from, separateRooms.to)

}