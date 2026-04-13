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

sealed class PublishIntent {
    object NavigateBack : PublishIntent()
    object PickImages : PublishIntent()
    data class ImagesSelectedBytes(val images: List<ByteArray>) : PublishIntent()
    object Submit : PublishIntent()
    object OpenCategorySelection : PublishIntent()
    object ClearPropertyType : PublishIntent()
    data class SetPropertyType(val type: PropertyType) : PublishIntent()

    data class SetTitle(val title: String) : PublishIntent()

    data class SetDealType(val type: DealType?) : PublishIntent()
    data class SetPrice(val price: String) : PublishIntent()
    data class SetCurrency(val currency: CurrencyType) : PublishIntent()

    data class SetCommercialPropertyType(val type: CommercialPropertyType?) : PublishIntent()
    data class SetCommercialRepairType(val type: CommercialRepairType?) : PublishIntent()

    data class SetArea(val value: Int?) : PublishIntent()
    data class SetSaleArea(val value: Int?) : PublishIntent()
    data class SetLandArea(val value: Int?) : PublishIntent()
    data class SetFloor(val value: Int?) : PublishIntent()
    data class SetFloorHouse(val value: Int?) : PublishIntent()

    data class SetCommercialAmenities(val amenities: Set<CommercialAmenity>) : PublishIntent()
    data class SetBuildingAmenities(val amenities: Set<BuildingAmenity>) : PublishIntent()
    data class SetHouseAmenities(val amenities: Set<HouseAmenity>) : PublishIntent()

    data class SetRoomsType(val type: RoomsType?) : PublishIntent()
    data class SetRoomsForSaleType(val type: RoomsType?) : PublishIntent()
    data class SetWalkthroughRoom(val value: Boolean) : PublishIntent()

    data class SetLivingArea(val value: Int?) : PublishIntent()
    data class SetKitchenArea(val value: Int?) : PublishIntent()

    data class SetBalconyType(val type: BalconyType?) : PublishIntent()
    data class SetBathroomType(val type: BathroomType?) : PublishIntent()

    data class SetRepairType(val type: ApartmentRepairType?) : PublishIntent()
    data class SetCeilingHeight(val type: CeilingHeightType?) : PublishIntent()

    data class SetWallMaterial(val type: WallMaterialType?) : PublishIntent()

    data class SetYearBuilt(val value: Int?) : PublishIntent()

    data class SetRoofType(val type: RoofType?) : PublishIntent()
    data class SetHeatingType(val type: HeatingType?) : PublishIntent()
    data class SetWaterType(val type: WaterType?) : PublishIntent()
    data class SetGasType(val type: GasType?) : PublishIntent()
    data class SetHouseType(val type: HouseType?) : PublishIntent()
    data class SetParkingType(val type: ParkingType?) : PublishIntent()
}