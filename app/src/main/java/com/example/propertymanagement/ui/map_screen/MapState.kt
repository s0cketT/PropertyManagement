package com.example.propertymanagement.ui.map_screen

import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.UserLocation


data class MapState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val currentUserId: String? = null,

    val userLocation: UserLocation? = null,

    val currencyRates: Map<String, CurrencyRate> = emptyMap(),
    /** Комиссия покупателя для подписей на метках (каталог). */
    val managerCommissionPercent: Double = 0.0,
    /** Сохранённые фильтры (тот же источник, что и список объявлений). */
    val filtersProperty: FiltersProperty? = null,

    val markers: List<Property> = emptyList(),
    val filteredMarkers: List<Property> = emptyList(),

    /** Выбранная метка: показываем bottom sheet. */
    val selectedMarkerProperty: Property? = null,

    val selectedStatuses: Set<PropertyStatus> = emptySet(),
    val selectedTypes: Set<PropertyType> = emptySet(),

    /** Показывать на карте только объявления из избранного (требуется авторизация). */
    val showFavoritesOnly: Boolean = false,
)
