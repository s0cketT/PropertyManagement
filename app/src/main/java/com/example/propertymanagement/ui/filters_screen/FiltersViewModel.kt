package com.example.propertymanagement.ui.filters_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.forMainCatalogDisplay
import com.example.propertymanagement.domain.use_case.FilterPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetFilterPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.SaveSelectedFiltersMarkerUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.extensions.clearPricePerMeter
import com.example.propertymanagement.ui.mapper.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FiltersViewModel(
    private val getFilterPropertyUseCase: GetFilterPropertyUseCase,
    private val saveSelectedFiltersMarkerUseCase: SaveSelectedFiltersMarkerUseCase,
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val filterPropertiesUseCase: FilterPropertiesUseCase,
    private val getTodayRatesUseCase: GetTodayRatesUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(FiltersState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<FiltersEvent>(viewModelScope)
    val event = _event.flow

    private var sourceProperties: List<Property> = emptyList()
    private var currencyRates: Map<String, CurrencyRate> = emptyMap()

    init {
        loadSelectedMarker()
        loadPreviewSourceData()
    }

    private fun loadSelectedMarker() {
        getFilterPropertyUseCase()
            .distinctUntilChanged()
            .onEach { filterProperty ->
                Log.d("!!!", filterProperty.toString())
                _state.update {
                    filterProperty?.toState() ?: FiltersState()
                }
            }
            .launchIn(viewModelScope)
    }

    fun processIntent(intent: FiltersIntent) {
        when (intent) {

            is FiltersIntent.ClearFilters -> {
                _state.update {
                    FiltersState()
                }
            }

            is FiltersIntent.SaveFilters -> {
                viewModelScope.launch {
                    saveSelectedFiltersMarkerUseCase(
                        state.value.toFiltersProperty()
                    )
                    _event.emit(FiltersEvent.NavigateBack)
                }
            }

            is FiltersIntent.NavigateBack -> {
                _event.emit(FiltersEvent.NavigateBack)
            }

            is FiltersIntent.NavigateToCategorySelection -> {
                _event.emit(FiltersEvent.NavigateToCategorySelection)
            }

            is FiltersIntent.NavigateToRegionSelection -> {
                _event.emit(FiltersEvent.NavigateToRegionSelection)
            }

            is FiltersIntent.SelectPropertyType -> {
                _state.update {
                    it.copy(
                        selectedPropertyType = intent.type,
                        windowViews = if (intent.type == PropertyType.APARTMENT || intent.type == PropertyType.ROOM) {
                            it.windowViews
                        } else {
                            emptySet()
                        }
                    )
                }
            }

            is FiltersIntent.SelectRegion -> {
                _state.update {
                    it.copy(
                        selectedRegionId = intent.id,
                        selectedRegionName = intent.name,
                        selectedCityIds = emptySet(),
                        selectedCityNames = emptySet(),
                        selectedLocationLat = null,
                        selectedLocationLng = null,
                    )
                }
            }

            is FiltersIntent.SelectCities -> {
                _state.update {
                    it.copy(
                        selectedCityIds = intent.cityIds,
                        selectedCityNames = intent.cityNames,
                    )
                }
            }

            is FiltersIntent.SelectLocationCoordinate -> {
                _state.update {
                    it.copy(
                        selectedLocationLat = intent.lat,
                        selectedLocationLng = intent.lng,
                    )
                }
            }

            is FiltersIntent.ClearLocationSelection -> {
                _state.update {
                    it.copy(
                        selectedRegionId = null,
                        selectedRegionName = null,
                        selectedCityIds = emptySet(),
                        selectedCityNames = emptySet(),
                        selectedLocationLat = null,
                        selectedLocationLng = null,
                    )
                }
            }

            is FiltersIntent.CurrencyChanged -> {
                _state.update { it.copy(selectedCurrency = intent.currency) }
            }

            is FiltersIntent.PriceChanged -> {
                _state.update { it.copy(price = intent.range) }
            }

            is FiltersIntent.PricePerMeterChanged -> {
                _state.update { it.copy(pricePerMeter = intent.range) }
            }

            is FiltersIntent.ClearPropertyType -> {
                _state.update {
                    FiltersState(
                        selectedRegionId = it.selectedRegionId,
                        selectedRegionName = it.selectedRegionName,
                        selectedCityIds = it.selectedCityIds,
                        selectedCityNames = it.selectedCityNames,
                        selectedLocationLat = it.selectedLocationLat,
                        selectedLocationLng = it.selectedLocationLng,
                        selectedCurrency = it.selectedCurrency,
                        price = it.price,
                        sellerType = it.sellerType,
                        onlyWithPhotos = it.onlyWithPhotos,
                        sortType = it.sortType
                    )
                }
            }

            is FiltersIntent.SellerTypeChanged -> {
                _state.update { it.copy(sellerType = intent.type) }
            }

            is FiltersIntent.OnlyWithPhotosChanged -> {
                _state.update { it.copy(onlyWithPhotos = intent.enabled) }
            }

            is FiltersIntent.SortChanged -> {
                _state.update { it.copy(sortType = intent.type) }
            }

            is FiltersIntent.CommercialDealTypeChanged -> {
                _state.update { state ->
                    val updated = state.copy(dealType = intent.type)

                    if (intent.type == null) {
                        updated.copy(
                            commercialPropertyType = null
                        ).clearPricePerMeter()
                    } else {
                        updated
                    }
                }
            }

            is FiltersIntent.CommercialPropertyTypeChanged -> {
                _state.update { it.copy(commercialPropertyType = intent.type) }
            }


            is FiltersIntent.AreaChanged -> {
                _state.update { it.copy(area = intent.range) }
            }

            is FiltersIntent.FloorChanged -> {
                _state.update { it.copy(floor = intent.range) }
            }

            is FiltersIntent.FloorHouseChanged -> {
                _state.update { it.copy(floorHouse = intent.range) }
            }

            is FiltersIntent.SeparateRoomsChanged -> {
                _state.update { it.copy(separateRooms = intent.range) }
            }

            is FiltersIntent.AmenitiesChanged -> {
                _state.update { it.copy(commercialAmenities = intent.amenities) }
            }

            is FiltersIntent.RoomsTypeChanged -> {
                _state.update { it.copy(roomsType = intent.type) }
            }

            is FiltersIntent.WalkthroughChanged -> {
                _state.update { it.copy(isWalkthroughRoom = intent.value) }
            }

            is FiltersIntent.LivingAreaChanged -> {
                _state.update { it.copy(livingArea = intent.range) }
            }

            is FiltersIntent.KitchenAreaChanged -> {
                _state.update { it.copy(kitchenArea = intent.range) }
            }

            is FiltersIntent.BathroomTypeChanged -> {
                _state.update { it.copy(bathroomType = intent.type) }
            }

            is FiltersIntent.BalconyTypeChanged -> {
                _state.update { it.copy(balconyType = intent.type) }
            }

            is FiltersIntent.CeilingHeightChanged -> {
                _state.update { it.copy(ceilingHeight = intent.type) }
            }

            is FiltersIntent.RepairTypeChanged -> {
                _state.update { it.copy(repairType = intent.type) }
            }

            is FiltersIntent.WallMaterialChanged -> {
                _state.update { it.copy(wallMaterial = intent.type) }
            }

            is FiltersIntent.WindowViewsChanged -> {
                _state.update { it.copy(windowViews = intent.views) }
            }

            is FiltersIntent.YearBuiltChanged -> {
                _state.update { it.copy(yearBuilt = intent.year) }
            }

            is FiltersIntent.BuildingAmenitiesChanged -> {
                _state.update { it.copy(buildingAmenities = intent.amenities) }
            }

            is FiltersIntent.CommercialRepairTypeChanged -> {
                _state.update { it.copy(commercialRepairType = intent.type) }
            }

            is FiltersIntent.RoomsForSaleChanged -> {
                _state.update { it.copy(roomsForSale = intent.type) }
            }

            is FiltersIntent.SaleAreaChanged -> {
                _state.update { it.copy(saleArea = intent.range) }
            }

            is FiltersIntent.HouseTypeChanged -> {
                _state.update { it.copy(houseType = intent.type) }
            }

            is FiltersIntent.LandAreaChanged -> {
                _state.update { it.copy(landArea = intent.range) }
            }

            is FiltersIntent.RoofTypeChanged -> {
                _state.update { it.copy(roofType = intent.type) }
            }

            is FiltersIntent.HeatingTypeChanged -> {
                _state.update { it.copy(heatingType = intent.type) }
            }

            is FiltersIntent.WaterTypeChanged -> {
                _state.update { it.copy(waterType = intent.type) }
            }

            is FiltersIntent.GasTypeChanged -> {
                _state.update { it.copy(gasType = intent.type) }
            }

            is FiltersIntent.HouseAmenitiesChanged -> {
                _state.update { it.copy(houseAmenities = intent.amenities) }
            }

            is FiltersIntent.ParkingTypeChanged -> {
                _state.update { it.copy(parkingType = intent.type) }
            }
        }

        updateMatchedPropertiesCount()
    }

    private fun loadPreviewSourceData() {
        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.id

            currencyRates = when (val ratesResult = getTodayRatesUseCase()) {
                is Resource.Success -> ratesResult.data
                else -> emptyMap()
            }

            sourceProperties = when (val propertiesResult = getPropertiesUseCase(userId = userId)) {
                is Resource.Success -> propertiesResult.data.forMainCatalogDisplay()
                is Resource.Error -> emptyList()
            }

            updateMatchedPropertiesCount()
        }
    }

    private fun updateMatchedPropertiesCount() {
        val matchedCount = if (sourceProperties.isEmpty()) {
            0
        } else {
            filterPropertiesUseCase(
                properties = sourceProperties,
                filters = _state.value.toFiltersProperty(),
                rates = currencyRates,
            ).size
        }

        _state.update { current ->
            if (current.matchedPropertiesCount == matchedCount) {
                current
            } else {
                current.copy(matchedPropertiesCount = matchedCount)
            }
        }
    }
}

private fun FiltersState.toFiltersProperty(): FiltersProperty {
    return FiltersProperty(
        type = selectedPropertyType,
        selectedRegionId = selectedRegionId,
        selectedRegionName = selectedRegionName,
        selectedCityIds = selectedCityIds,
        selectedCityNames = selectedCityNames,
        selectedLocationLat = selectedLocationLat,
        selectedLocationLng = selectedLocationLng,
        price = price.let {
            IntRangeFilter(it.from?.toIntOrNull(), it.to?.toIntOrNull())
        },
        pricePerMeter = pricePerMeter.let {
            IntRangeFilter(it.from?.toIntOrNull(), it.to?.toIntOrNull())
        },
        area = area,
        floor = floor,
        floorHouse = floorHouse,
        separateRooms = separateRooms,
        selectedCurrency = selectedCurrency,
        selectedSellerType = sellerType,
        onlyWithPhotos = onlyWithPhotos,
        sortType = sortType,
        selectedDealType = dealType,
        selectedCommercialPropertyType = commercialPropertyType,
        commercialAmenities = commercialAmenities,
        commercialRepairType = commercialRepairType,
        roomsForSale = roomsForSale,
        saleArea = saleArea,
        roomsType = roomsType,
        isWalkthroughRoom = isWalkthroughRoom,
        livingArea = livingArea,
        kitchenArea = kitchenArea,
        bathroomType = bathroomType,
        balconyType = balconyType,
        ceilingHeight = ceilingHeight,
        repairType = repairType,
        wallMaterial = wallMaterial,
        windowViews = windowViews,
        yearBuilt = yearBuilt,
        buildingAmenities = buildingAmenities,
        houseType = houseType,
        landArea = landArea,
        roofType = roofType,
        heatingType = heatingType,
        waterType = waterType,
        gasType = gasType,
        houseAmenities = houseAmenities,
        parkingType = parkingType,
    )
}