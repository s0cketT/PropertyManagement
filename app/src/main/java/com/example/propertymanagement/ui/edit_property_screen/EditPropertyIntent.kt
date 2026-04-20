package com.example.propertymanagement.ui.edit_property_screen

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
import com.example.propertymanagement.domain.model.GeocodedAddressParts
import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.domain.model.HouseAmenity
import com.example.propertymanagement.domain.model.HouseType
import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WaterType

sealed class EditPropertyIntent {
    object NavigateBack : EditPropertyIntent()
    object PickImages : EditPropertyIntent()
    object ClearAllImages : EditPropertyIntent()
    data class ImagesSelectedBytes(val images: List<ByteArray>) : EditPropertyIntent()
    object Submit : EditPropertyIntent()
    object OpenCategorySelection : EditPropertyIntent()
    object ClearPropertyType : EditPropertyIntent()
    data class SetPropertyType(val type: PropertyType) : EditPropertyIntent()

    data class SetTitle(val title: String) : EditPropertyIntent()

    data class SetDescription(val text: String) : EditPropertyIntent()

    data class SetDealType(val type: DealType?) : EditPropertyIntent()
    data class SetPrice(val price: String) : EditPropertyIntent()
    data class SetCurrency(val currency: CurrencyType) : EditPropertyIntent()

    data class SetCommercialPropertyType(val type: CommercialPropertyType?) : EditPropertyIntent()
    data class SetCommercialRepairType(val type: CommercialRepairType?) : EditPropertyIntent()

    data class SetArea(val value: Int?) : EditPropertyIntent()
    data class SetSaleArea(val value: Int?) : EditPropertyIntent()
    data class SetLandArea(val value: Int?) : EditPropertyIntent()
    data class SetFloor(val value: Int?) : EditPropertyIntent()
    data class SetFloorHouse(val value: Int?) : EditPropertyIntent()

    data class SetCommercialAmenities(val amenities: Set<CommercialAmenity>) : EditPropertyIntent()
    data class SetBuildingAmenities(val amenities: Set<BuildingAmenity>) : EditPropertyIntent()
    data class SetHouseAmenities(val amenities: Set<HouseAmenity>) : EditPropertyIntent()

    data class SetRoomsType(val type: RoomsType?) : EditPropertyIntent()
    data class SetRoomsForSaleType(val type: RoomsType?) : EditPropertyIntent()
    data class SetWalkthroughRoom(val value: Boolean) : EditPropertyIntent()

    data class SetLivingArea(val value: Int?) : EditPropertyIntent()
    data class SetKitchenArea(val value: Int?) : EditPropertyIntent()

    data class SetBalconyType(val type: BalconyType?) : EditPropertyIntent()
    data class SetBathroomType(val type: BathroomType?) : EditPropertyIntent()

    data class SetRepairType(val type: ApartmentRepairType?) : EditPropertyIntent()
    data class SetCeilingHeight(val type: CeilingHeightType?) : EditPropertyIntent()

    data class SetWallMaterial(val type: WallMaterialType?) : EditPropertyIntent()

    data class SetYearBuilt(val value: Int?) : EditPropertyIntent()

    data class SetRoofType(val type: RoofType?) : EditPropertyIntent()
    data class SetHeatingType(val type: HeatingType?) : EditPropertyIntent()
    data class SetWaterType(val type: WaterType?) : EditPropertyIntent()
    data class SetGasType(val type: GasType?) : EditPropertyIntent()
    data class SetHouseType(val type: HouseType?) : EditPropertyIntent()
    data class SetParkingType(val type: ParkingType?) : EditPropertyIntent()

    data class SetAddressBottomSheetOpen(val open: Boolean) : EditPropertyIntent()
    data class SetMapPickerOpen(val open: Boolean) : EditPropertyIntent()
    data class SetAddressCountry(val value: String) : EditPropertyIntent()
    data class SetAddressRegion(val value: String) : EditPropertyIntent()
    data class SetAddressCity(val value: String) : EditPropertyIntent()
    data class SetAddressStreet(val value: String) : EditPropertyIntent()
    data class SetAddressHouse(val value: String) : EditPropertyIntent()

    data class AddressSheetDone(
        val latitude: Double?,
        val longitude: Double?,
    ) : EditPropertyIntent()

    data class ConfirmMapLocation(
        val latitude: Double,
        val longitude: Double,
        val geocoded: GeocodedAddressParts,
    ) : EditPropertyIntent()
}
