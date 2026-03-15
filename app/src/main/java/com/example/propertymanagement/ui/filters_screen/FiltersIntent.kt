package com.example.propertymanagement.ui.filters_screen

import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.domain.model.StringRangeFilter
import com.example.propertymanagement.domain.model.IntRangeFilter

sealed class FiltersIntent {
    data object NavigateBack : FiltersIntent()
    data object NavigateToCategorySelection : FiltersIntent()

    data class SelectPropertyType(val type: PropertyType) : FiltersIntent()
    data object SaveFilters : FiltersIntent()
    data object ClearFilters : FiltersIntent()
    data object ClearPropertyType : FiltersIntent()

    //работа с ценой
    data class PriceChanged(val range: StringRangeFilter) : FiltersIntent()
    data class CurrencyChanged(val currency: CurrencyType) : FiltersIntent()
    //работа с ценой за м²
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
}