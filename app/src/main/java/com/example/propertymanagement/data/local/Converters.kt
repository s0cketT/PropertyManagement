package com.example.propertymanagement.data.local

import androidx.room.TypeConverter
import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.SellerType

class Converters {

    @TypeConverter
    fun fromPropertyType(value: PropertyType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toPropertyType(value: String?): PropertyType? {
        return value?.let { PropertyType.valueOf(it) }
    }

    @TypeConverter
    fun fromCurrencyType(value: CurrencyType): String {
        return value.name
    }

    @TypeConverter
    fun toCurrencyType(value: String): CurrencyType {
        return CurrencyType.valueOf(value)
    }

    // Новые конвертеры для SellerType (аналогично другим enum)
    @TypeConverter
    fun fromSellerType(value: SellerType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toSellerType(value: String?): SellerType? {
        return value?.let { SellerType.valueOf(it) }
    }

    @TypeConverter
    fun fromCommercialAmenities(value: Set<CommercialAmenity>?): String? {
        return value?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toCommercialAmenities(value: String?): Set<CommercialAmenity> {
        return value
            ?.split(",")
            ?.mapNotNull { name ->
                try {
                    CommercialAmenity.valueOf(name)
                } catch (_: IllegalArgumentException) {
                    null
                }
            }
            ?.toSet() ?: emptySet()
    }
}