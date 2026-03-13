package com.example.propertymanagement.ui.filters_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.domain.model.StringRangeFilter
import com.example.propertymanagement.domain.use_case.GetSelectedPropertyMarkerUseCase
import com.example.propertymanagement.domain.use_case.SaveSelectedFiltersMarkerUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.extensions.clearIntRanges
import com.example.propertymanagement.ui.extensions.clearPrice
import com.example.propertymanagement.ui.extensions.clearPricePerMeter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FiltersViewModel(
    private val getSelectedPropertyMarkerUseCase: GetSelectedPropertyMarkerUseCase,
    private val saveSelectedFiltersMarkerUseCase: SaveSelectedFiltersMarkerUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(FiltersState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<FiltersEvent>(viewModelScope)
    val event = _event.flow

    init {
        loadSelectedMarker()
    }

    private fun loadSelectedMarker() {
        getSelectedPropertyMarkerUseCase()
            .distinctUntilChanged()
            .onEach { filterProperty ->
                Log.d("!!!", filterProperty.toString())
                _state.update {
                    it.copy(
                        selectedPropertyType = filterProperty?.type,

                        price = StringRangeFilter(
                            from = filterProperty?.price?.from?.toString(),
                            to = filterProperty?.price?.to?.toString()
                        ),

                        pricePerMeter = StringRangeFilter(
                            from = filterProperty?.pricePerMeter?.from?.toString(),
                            to = filterProperty?.pricePerMeter?.to?.toString()
                        ),

                        area = filterProperty?.area ?: IntRangeFilter(),
                        floor = filterProperty?.floor ?: IntRangeFilter(),
                        floorHouse = filterProperty?.floorHouse ?: IntRangeFilter(),

                        selectedCurrency = filterProperty?.selectedCurrency ?: CurrencyType.USD,

                        selectedSellerType = filterProperty?.selectedSellerType,
                        onlyWithPhotos = filterProperty?.onlyWithPhotos ?: false,
                        sortType = filterProperty?.sortType ?: SortType.NEWEST,
                        selectedDealType = filterProperty?.selectedDealType,
                        selectedCommercialPropertyType = filterProperty?.selectedCommercialPropertyType
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun processIntent(intent: FiltersIntent) {
        when (intent) {

            is FiltersIntent.ClearFilters -> {
                _state.update {
                    it.copy(
                        selectedPropertyType = null,

                        selectedSellerType = null,
                        onlyWithPhotos = false,
                        sortType = SortType.NEWEST,
                        selectedDealType = null,
                        selectedCommercialPropertyType = null
                        )
                        .clearPricePerMeter()
                        .clearPrice()
                        .clearIntRanges()
                }
            }

            is FiltersIntent.SaveFilters -> {
                viewModelScope.launch {
                    saveSelectedFiltersMarkerUseCase(
                        FiltersProperty(
                            type = state.value.selectedPropertyType,

                            price = state.value.price.let { range ->
                                IntRangeFilter(
                                    from = range.from?.toIntOrNull(),
                                    to = range.to?.toIntOrNull()
                                )
                            },

                            pricePerMeter = state.value.pricePerMeter.let { range ->
                                IntRangeFilter(
                                    from = range.from?.toIntOrNull(),
                                    to = range.to?.toIntOrNull()
                                )
                            },

                            area = state.value.area,
                            floor = state.value.floor,
                            floorHouse = state.value.floorHouse,

                            selectedCurrency = state.value.selectedCurrency,
                            selectedSellerType = state.value.selectedSellerType,
                            onlyWithPhotos = state.value.onlyWithPhotos,
                            sortType = state.value.sortType,
                            selectedDealType = state.value.selectedDealType,
                            selectedCommercialPropertyType = state.value.selectedCommercialPropertyType
                        )
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

            is FiltersIntent.SelectPropertyType -> {
                _state.update {
                    it.copy(
                        selectedPropertyType = intent.type,
                    )
                }
            }

            is FiltersIntent.CurrencyChanged -> {
                _state.update {
                    it.copy(selectedCurrency = intent.currency)
                }
            }

            is FiltersIntent.PriceChanged -> {
                _state.update {
                    it.copy(price = intent.range)
                }
            }

            is FiltersIntent.PricePerMeterChanged -> {
                _state.update {
                    it.copy(pricePerMeter = intent.range)
                }
            }

            is FiltersIntent.ClearPropertyType -> {
                _state.update {
                    it.copy(
                        selectedPropertyType = null,
                        selectedDealType = null,
                        selectedCommercialPropertyType = null
                        )
                        .clearPricePerMeter()
                        .clearIntRanges()
                }
            }

            is FiltersIntent.SellerTypeChanged -> {
                _state.update {
                    it.copy(selectedSellerType = intent.type)
                }
            }

            is FiltersIntent.OnlyWithPhotosChanged -> {
                _state.update { it.copy(onlyWithPhotos = intent.enabled) }
            }

            is FiltersIntent.SortChanged -> {
                _state.update {
                    it.copy(sortType = intent.type)
                }
            }

            is FiltersIntent.CommercialDealTypeChanged -> {
                _state.update { state ->
                    val updated = state.copy(selectedDealType = intent.type)

                    if (intent.type == null) {
                        updated.copy(
                            selectedCommercialPropertyType = null
                        ).clearPricePerMeter()
                    } else {
                        updated
                    }
                }
            }

            is FiltersIntent.CommercialPropertyTypeChanged -> {
                _state.update {
                    it.copy(selectedCommercialPropertyType = intent.type)
                }
            }


            is FiltersIntent.AreaChanged -> {
                _state.update {
                    it.copy(area = intent.range)
                }
            }

            is FiltersIntent.FloorChanged -> {
                _state.update {
                    it.copy(floor = intent.range)
                }
            }

            is FiltersIntent.FloorHouseChanged -> {
                _state.update {
                    it.copy(floorHouse = intent.range)
                }
            }
        }
    }
}