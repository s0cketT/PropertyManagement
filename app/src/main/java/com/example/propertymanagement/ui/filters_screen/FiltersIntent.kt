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
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.domain.model.StringRangeFilter
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WaterType

sealed class FiltersIntent {
    data object NavigateBack : FiltersIntent()
    data object NavigateToCategorySelection : FiltersIntent()
    data object NavigateToRegionSelection : FiltersIntent()

    data class SelectPropertyType(val type: PropertyType) : FiltersIntent()
    data class SelectRegion(val id: Long, val name: String) : FiltersIntent()
    data class SelectCities(val cityIds: Set<Long>, val cityNames: Set<String>) : FiltersIntent()
    data class SelectLocationCoordinate(val lat: Double?, val lng: Double?) : FiltersIntent()
    data object ClearLocationSelection : FiltersIntent()
    data object SaveFilters : FiltersIntent()
    data object ClearFilters : FiltersIntent()
    data object ClearPropertyType : FiltersIntent()

    data class PriceChanged(val range: StringRangeFilter) : FiltersIntent()
    data class CurrencyChanged(val currency: CurrencyType) : FiltersIntent()
    data class PricePerMeterChanged(val range: StringRangeFilter) : FiltersIntent()

    data class SellerTypeChanged(val type: SellerType?) : FiltersIntent()
    data class OnlyWithPhotosChanged(val enabled: Boolean) : FiltersIntent()
    data class SortChanged(val type: SortType) : FiltersIntent()
    data class CommercialDealTypeChanged(val type: DealType?) : FiltersIntent()
    data class CommercialPropertyTypeChanged(val type: CommercialPropertyType?) : FiltersIntent()


    data class AreaChanged(val range: IntRangeFilter) : FiltersIntent()
    data class FloorChanged(val range: IntRangeFilter) : FiltersIntent()
    data class FloorHouseChanged(val range: IntRangeFilter) : FiltersIntent()
    data class SeparateRoomsChanged(val range: IntRangeFilter) : FiltersIntent()

    data class AmenitiesChanged(val amenities: Set<CommercialAmenity>) : FiltersIntent()
    data class CommercialRepairTypeChanged(val type: CommercialRepairType?) : FiltersIntent()

    data class RoomsForSaleChanged(val type: RoomsType?) : FiltersIntent()
    data class SaleAreaChanged(val range: IntRangeFilter) : FiltersIntent()

    // Apartment
    data class RoomsTypeChanged(val type: RoomsType?) : FiltersIntent()
    data class WalkthroughChanged(val value: Boolean) : FiltersIntent()
    data class LivingAreaChanged(val range: IntRangeFilter) : FiltersIntent()
    data class KitchenAreaChanged(val range: IntRangeFilter) : FiltersIntent()
    data class BathroomTypeChanged(val type: BathroomType?) : FiltersIntent()
    data class BalconyTypeChanged(val type: BalconyType?) : FiltersIntent()
    data class CeilingHeightChanged(val type: CeilingHeightType?) : FiltersIntent()
    data class RepairTypeChanged(val type: ApartmentRepairType?) : FiltersIntent()
    data class WallMaterialChanged(val type: WallMaterialType?) : FiltersIntent()
    data class YearBuiltChanged(val year: Int?) : FiltersIntent()
    data class BuildingAmenitiesChanged(val amenities: Set<BuildingAmenity>) : FiltersIntent()

    data class HouseTypeChanged(val type: HouseType?) : FiltersIntent()
    data class LandAreaChanged(val range: IntRangeFilter) : FiltersIntent()
    data class RoofTypeChanged(val type: RoofType?) : FiltersIntent()
    data class HeatingTypeChanged(val type: HeatingType?) : FiltersIntent()
    data class WaterTypeChanged(val type: WaterType?) : FiltersIntent()
    data class GasTypeChanged(val type: GasType?) : FiltersIntent()
    data class HouseAmenitiesChanged(val amenities: Set<HouseAmenity>) : FiltersIntent()

    data class ParkingTypeChanged(val type: ParkingType?) : FiltersIntent()
}
