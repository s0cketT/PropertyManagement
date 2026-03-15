package com.example.propertymanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.SortType

@Entity(tableName = "selected_property_marker")
data class FiltersPropertyDbModel(
    @PrimaryKey
    val id: Int,
    val type: PropertyType?,

    val priceFrom: Int?,
    val priceTo: Int?,
    val pricePerMeterFrom: Int?,
    val pricePerMeterTo: Int?,
    val areaTo: Int?,
    val areaFrom: Int?,
    val floorTo: Int?,
    val floorFrom: Int?,
    val floorHouseTo: Int?,
    val floorHouseFrom: Int?,
    val separateRoomsFrom: Int?,
    val separateRoomsTo: Int?,

    val selectedCurrency: CurrencyType,

    val selectedSellerType: SellerType?,
    val onlyWithPhotos: Boolean,
    val sortType: SortType,
    val selectedDealType: DealType?,
    val selectedCommercialPropertyType: CommercialPropertyType?,

    val commercialAmenities: Set<CommercialAmenity>,
)