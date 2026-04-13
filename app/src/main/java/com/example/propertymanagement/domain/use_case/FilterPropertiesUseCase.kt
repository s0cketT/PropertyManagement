package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.ui.mapper.toCode

class FilterPropertiesUseCase {

    operator fun invoke(
        properties: List<Property>,
        filters: FiltersProperty,
        rates: Map<String, CurrencyRate>
    ): List<Property> {

        return properties
            .asSequence()
            .filter { it.matchesFilters(filters, rates) }
            .sortedWith(getComparator(filters.sortType, rates))
            .toList()
    }

    private fun getComparator(
        sortType: SortType,
        rates: Map<String, CurrencyRate>
    ): Comparator<Property> {

        return when (sortType) {
            SortType.NEWEST -> compareByDescending { it.id }
            SortType.PRICE_ASC -> compareBy { toBYN(it.price, it.currency, rates) }
            SortType.PRICE_DESC -> compareByDescending { toBYN(it.price, it.currency, rates) }
        }
    }

    private fun Property.matchesFilters(
        f: FiltersProperty,
        rates: Map<String, CurrencyRate>
    ): Boolean {

        if (f.type != null && type != f.type) return false
        if (f.selectedDealType != null && dealType != f.selectedDealType) return false

        if (f.onlyWithPhotos && photos.isEmpty()) return false

        val priceInBYN = toBYN(
            price = price,
            currency = currency,
            rates = rates
        )

        if (!priceInBYN.inRange(f.price)) return false

        area?.inRange(f.area)?.let { if (!it) return false }

        if (!floor.inRange(f.floor)) return false
        if (!totalFloors.inRange(f.floorHouse)) return false

        if (f.roomsType != null && rooms != f.roomsType) return false
        if (f.yearBuilt != null && yearBuilt != f.yearBuilt) return false

        if (f.buildingAmenities.isNotEmpty() &&
            !buildingAmenities.containsAll(f.buildingAmenities)
        ) return false

        if (f.houseAmenities.isNotEmpty() &&
            !houseAmenities.containsAll(f.houseAmenities)
        ) return false

        if (f.commercialAmenities.isNotEmpty() &&
            !commercialAmenities.containsAll(f.commercialAmenities)
        ) return false

        return true
    }

    private fun toBYN(
        price: Double,
        currency: CurrencyType,
        rates: Map<String, CurrencyRate>
    ): Double {

        if (currency == CurrencyType.BYN) return price

        val rate = rates[currency.toCode()]?.ratePerUnit ?: 1.0
        return price * rate
    }

    private fun Double?.inRange(range: IntRangeFilter): Boolean {
        val value = this ?: return true
        val from = range.from?.toDouble()
        val to = range.to?.toDouble()

        return (from == null || value >= from) &&
                (to == null || value <= to)
    }

    private fun Int?.inRange(range: IntRangeFilter): Boolean {
        if (this == null) return true

        return (range.from == null || this >= range.from) &&
                (range.to == null || this <= range.to)
    }
}