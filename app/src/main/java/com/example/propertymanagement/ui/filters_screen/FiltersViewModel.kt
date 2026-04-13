package com.example.propertymanagement.ui.filters_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.use_case.GetFilterPropertyUseCase
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
) : ViewModel() {
    private val _state = MutableStateFlow(FiltersState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<FiltersEvent>(viewModelScope)
    val event = _event.flow

    init {
        loadSelectedMarker()
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
                        FiltersProperty(
                            type = state.value.selectedPropertyType,

                            price = state.value.price.let {
                                IntRangeFilter(it.from?.toIntOrNull(), it.to?.toIntOrNull())
                            },

                            pricePerMeter = state.value.pricePerMeter.let {
                                IntRangeFilter(it.from?.toIntOrNull(), it.to?.toIntOrNull())
                            },

                            area = state.value.area,
                            floor = state.value.floor,
                            floorHouse = state.value.floorHouse,
                            separateRooms = state.value.separateRooms,

                            selectedCurrency = state.value.selectedCurrency,
                            selectedSellerType = state.value.sellerType,
                            onlyWithPhotos = state.value.onlyWithPhotos,
                            sortType = state.value.sortType,
                            selectedDealType = state.value.dealType,
                            selectedCommercialPropertyType = state.value.commercialPropertyType,

                            commercialAmenities = state.value.commercialAmenities,
                            commercialRepairType = state.value.commercialRepairType,

                            roomsForSale = state.value.roomsForSale,
                            saleArea = state.value.saleArea,

                            roomsType = state.value.roomsType,
                            isWalkthroughRoom = state.value.isWalkthroughRoom,
                            livingArea = state.value.livingArea,
                            kitchenArea = state.value.kitchenArea,
                            bathroomType = state.value.bathroomType,
                            balconyType = state.value.balconyType,
                            ceilingHeight = state.value.ceilingHeight,
                            repairType = state.value.repairType,
                            wallMaterial = state.value.wallMaterial,
                            yearBuilt = state.value.yearBuilt,
                            buildingAmenities = state.value.buildingAmenities,

                            houseType = state.value.houseType,
                            landArea = state.value.landArea,
                            roofType = state.value.roofType,
                            heatingType = state.value.heatingType,
                            waterType = state.value.waterType,
                            gasType = state.value.gasType,
                            houseAmenities = state.value.houseAmenities,

                            parkingType = state.value.parkingType
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
    }
}