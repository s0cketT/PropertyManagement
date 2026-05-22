package com.example.propertymanagement.ui.property_detail_screen

import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetailPrices

data class PropertyDetailState(
    val isLoading: Boolean = true,
    val property: Property? = null,
    val error: String? = null,
    val notFound: Boolean = false,
    val isMapFullscreen: Boolean = false,
    val isImageViewerOpen: Boolean = false,
    val imageViewerInitialPage: Int = 0,
    val convertedPrices: PropertyDetailPrices? = null,
    /** Курсы для пересчёта комиссии в [detailPriceLeadCurrency] на листе заявки. */
    val currencyRates: Map<String, CurrencyRate> = emptyMap(),
    /**
     * Ведущая валюта цены для каталога (из фильтра или USD); для «Мои объявления» игнорируется в UI.
     */
    val detailPriceLeadCurrency: CurrencyType = CurrencyType.USD,
    /** Процент комиссии менеджера для покупателя (0 — экран «мои объявления» без надбавки). */
    val managerCommissionPercent: Double = 0.0,
    val isApplicationSheetOpen: Boolean = false,
    val applicationComment: String = "",
    val isSubmittingApplication: Boolean = false,

    /** Объекты OSM в радиусе по умолчанию 1,5 км (школы, поликлиники, продуктовые магазины). */
    val nearbyMapPois: List<NearbyMapPoi> = emptyList(),
    /** Категории, отмеченные на карте чекбоксами (по умолчанию все выключены). */
    val visiblePoiCategories: Set<NearbyPoiCategory> = emptySet(),
    val isNearbyPoisLoading: Boolean = false,
    /** Ошибка загрузки Overpass; текст ошибки в UI из stringResource. */
    val nearbyPoisLoadFailed: Boolean = false,
)
