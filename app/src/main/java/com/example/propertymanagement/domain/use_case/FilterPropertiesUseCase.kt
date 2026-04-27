package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.domain.currency.convertAmountToByn
import java.time.Instant

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
            SortType.NEWEST -> compareByDescending<Property> { it.createdAt ?: Instant.EPOCH }
                .thenByDescending { it.id }
            SortType.PRICE_ASC -> compareBy { convertAmountToByn(it.price, it.currency, rates) }
            SortType.PRICE_DESC -> compareByDescending { convertAmountToByn(it.price, it.currency, rates) }
        }
    }

    private fun Property.matchesFilters(
        f: FiltersProperty,
        rates: Map<String, CurrencyRate>
    ): Boolean {

        if (f.type != null && type != f.type) return false
        if (f.selectedDealType != null && dealType != f.selectedDealType) return false
        if (!region.matchesSelectedRegion(f.selectedRegionName)) return false
        if (!city.matchesSelectedCities(f.selectedCityNames)) return false

        if (f.onlyWithPhotos && photos.isEmpty()) return false

        val priceInBYN = convertAmountToByn(price, currency, rates)

        if (!priceInBYN.inPriceFilterRange(f.price, f.selectedCurrency, rates)) return false

        if (!area.inRange(f.area)) return false

        if (!f.pricePerMeter.isEmpty()) {
            val a = area
            if (a == null || a <= 0.0) return false
            val pricePerMeterInBYN = priceInBYN / a
            if (!pricePerMeterInBYN.inPriceFilterRange(
                    f.pricePerMeter,
                    f.selectedCurrency,
                    rates
                )
            ) return false
        }

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

    private fun Double.inPriceFilterRange(
        range: IntRangeFilter,
        filterCurrency: CurrencyType,
        rates: Map<String, CurrencyRate>
    ): Boolean {
        val from = range.from?.let { convertAmountToByn(it.toDouble(), filterCurrency, rates) }
        val to = range.to?.let { convertAmountToByn(it.toDouble(), filterCurrency, rates) }
        return (from == null || this >= from) && (to == null || this <= to)
    }

    private fun Double?.inRange(range: IntRangeFilter): Boolean {
        if (this == null) return true

        return (range.from == null || this >= range.from) &&
                (range.to == null || this <= range.to)
    }

    private fun Int?.inRange(range: IntRangeFilter): Boolean {
        if (this == null) return true

        return (range.from == null || this >= range.from) &&
                (range.to == null || this <= range.to)
    }

    private fun String?.matchesSelectedRegion(selectedRegionName: String?): Boolean {
        if (selectedRegionName.isNullOrBlank()) {
            return true
        }

        return this.normalizeLocationName() == selectedRegionName.normalizeLocationName()
    }

    private fun String?.matchesSelectedCities(selectedCityNames: Set<String>): Boolean {
        if (selectedCityNames.isEmpty()) {
            return true
        }

        val cityName = this.normalizeLocationName()
        return selectedCityNames.any { selectedCity ->
            selectedCity.normalizeLocationName() == cityName
        }
    }

    private fun String?.normalizeLocationName(): String {
        return this.orEmpty()
            .trim()
            .lowercase()
            .replace('ё', 'е')
    }
}