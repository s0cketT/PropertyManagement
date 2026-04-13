package com.example.propertymanagement.data.local

import androidx.room.TypeConverter
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

class Converters {

    @TypeConverter
    fun fromPropertyType(value: PropertyType?) = value?.name

    @TypeConverter
    fun toPropertyType(value: String?) =
        value?.let { PropertyType.valueOf(it) }

    @TypeConverter
    fun fromCurrencyType(value: CurrencyType) = value.name

    @TypeConverter
    fun toCurrencyType(value: String) =
        CurrencyType.valueOf(value)

    @TypeConverter
    fun fromSellerType(value: SellerType?) = value?.name

    @TypeConverter
    fun toSellerType(value: String?) =
        value?.let { SellerType.valueOf(it) }

    @TypeConverter
    fun fromDealType(value: DealType?) = value?.name

    @TypeConverter
    fun toDealType(value: String?) =
        value?.let { DealType.valueOf(it) }

    @TypeConverter
    fun fromSortType(value: SortType) = value.name

    @TypeConverter
    fun toSortType(value: String) =
        SortType.valueOf(value)

    @TypeConverter
    fun fromCommercialPropertyType(value: CommercialPropertyType?) = value?.name

    @TypeConverter
    fun toCommercialPropertyType(value: String?) =
        value?.let { CommercialPropertyType.valueOf(it) }

    @TypeConverter
    fun fromCommercialRepairType(value: CommercialRepairType?) = value?.name

    @TypeConverter
    fun toCommercialRepairType(value: String?) =
        value?.let { CommercialRepairType.valueOf(it) }

    @TypeConverter
    fun fromRoomsType(value: RoomsType?) = value?.name

    @TypeConverter
    fun toRoomsType(value: String?) =
        value?.let { RoomsType.valueOf(it) }

    @TypeConverter
    fun fromBathroomType(value: BathroomType?) = value?.name

    @TypeConverter
    fun toBathroomType(value: String?) =
        value?.let { BathroomType.valueOf(it) }

    @TypeConverter
    fun fromBalconyType(value: BalconyType?) = value?.name

    @TypeConverter
    fun toBalconyType(value: String?) =
        value?.let { BalconyType.valueOf(it) }

    @TypeConverter
    fun fromCeilingHeightType(value: CeilingHeightType?) = value?.name

    @TypeConverter
    fun toCeilingHeightType(value: String?) =
        value?.let { CeilingHeightType.valueOf(it) }

    @TypeConverter
    fun fromApartmentRepairType(value: ApartmentRepairType?) = value?.name

    @TypeConverter
    fun toApartmentRepairType(value: String?) =
        value?.let { ApartmentRepairType.valueOf(it) }

    @TypeConverter
    fun fromWallMaterialType(value: WallMaterialType?) = value?.name

    @TypeConverter
    fun toWallMaterialType(value: String?) =
        value?.let { WallMaterialType.valueOf(it) }

    @TypeConverter
    fun fromHouseType(value: HouseType?) = value?.name

    @TypeConverter
    fun toHouseType(value: String?) =
        value?.let { HouseType.valueOf(it) }

    @TypeConverter
    fun fromRoofType(value: RoofType?) = value?.name

    @TypeConverter
    fun toRoofType(value: String?) =
        value?.let { RoofType.valueOf(it) }

    @TypeConverter
    fun fromHeatingType(value: HeatingType?) = value?.name

    @TypeConverter
    fun toHeatingType(value: String?) =
        value?.let { HeatingType.valueOf(it) }

    @TypeConverter
    fun fromWaterType(value: WaterType?) = value?.name

    @TypeConverter
    fun toWaterType(value: String?) =
        value?.let { WaterType.valueOf(it) }

    @TypeConverter
    fun fromGasType(value: GasType?) = value?.name

    @TypeConverter
    fun toGasType(value: String?) =
        value?.let { GasType.valueOf(it) }

    @TypeConverter
    fun fromParkingType(value: ParkingType?) = value?.name

    @TypeConverter
    fun toParkingType(value: String?) =
        value?.let { ParkingType.valueOf(it) }
}