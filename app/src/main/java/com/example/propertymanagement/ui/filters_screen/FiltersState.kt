package com.example.propertymanagement.ui.filters_screen


import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.domain.model.StringRangeFilter
import com.example.propertymanagement.ui.extensions.isRangeValid

data class FiltersState(
    val selectedPropertyType:  PropertyType? = null,

    val selectedCurrency: CurrencyType = CurrencyType.USD,

    val price: StringRangeFilter = StringRangeFilter(),
    val pricePerMeter: StringRangeFilter = StringRangeFilter(),

    val selectedSellerType: SellerType? = null,
    val onlyWithPhotos: Boolean = false,
    val sortType: SortType = SortType.NEWEST,
    val selectedDealType: DealType? = null,
    val selectedCommercialPropertyType: CommercialPropertyType? = null,

    val area: IntRangeFilter = IntRangeFilter(),
    val floor: IntRangeFilter = IntRangeFilter(),
    val floorHouse: IntRangeFilter = IntRangeFilter(),

    ) {
    val isFiltersValid: Boolean
        get() = isRangeValid(price.from?.toIntOrNull(), price.to?.toIntOrNull()) &&
                isRangeValid(pricePerMeter.from?.toIntOrNull(), pricePerMeter.to?.toIntOrNull()) &&
                isRangeValid(area.from, area.to) &&
                isRangeValid(floor.from, floor.to) &&
                isRangeValid(floorHouse.from, floorHouse.to)

}