package com.example.propertymanagement.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.domain.model.forMainCatalogDisplay
import com.example.propertymanagement.domain.use_case.FilterPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetFilterPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.map_screen.MapEvent
import com.example.propertymanagement.ui.map_screen.MapIntent
import com.example.propertymanagement.ui.map_screen.MapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val observeLocationUseCase: ObserveLocationUseCase,
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getFilterPropertyUseCase: GetFilterPropertyUseCase,
    private val filterPropertiesUseCase: FilterPropertiesUseCase,
    private val getTodayRatesUseCase: GetTodayRatesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<MapEvent>(viewModelScope)
    val event = _event.flow

    init {
        loadProperties()
        subscribeSavedFilters()
    }

    fun processIntent(intent: MapIntent) {

        when (intent) {

            is MapIntent.LoadUserLocation -> {
                observeLocation()
            }

            is MapIntent.NavigateBack -> {
                _event.emit(MapEvent.NavigateBack)
            }

            is MapIntent.ApplyStatusFilter -> {
                _state.update { current ->

                    val newSelected = when (intent.status) {
                        null -> emptySet()
                        else -> if (intent.status in current.selectedStatuses) {
                            current.selectedStatuses - intent.status
                        } else {
                            current.selectedStatuses + intent.status
                        }
                    }

                    current.copy(selectedStatuses = newSelected)
                }
                recomputeFilteredMarkers()
            }

            is MapIntent.ApplyTypeFilter -> {
                _state.update { current ->
                    val newSelectedTypes = when (intent.type) {
                        null -> emptySet()
                        else -> if (intent.type in current.selectedTypes) {
                            current.selectedTypes - intent.type
                        } else {
                            current.selectedTypes + intent.type
                        }
                    }

                    current.copy(
                        selectedTypes = newSelectedTypes,
                        selectedMarkerProperty = null
                    )
                }
                recomputeFilteredMarkers()
            }

            is MapIntent.NavigateFilterScreen -> {
                _event.emit(MapEvent.NavigateFilterScreen)
            }

            is MapIntent.MarkerTapped -> {
                _state.update { it.copy(selectedMarkerProperty = intent.property) }
            }

            is MapIntent.DismissMarkerBottomSheet -> {
                _state.update { it.copy(selectedMarkerProperty = null) }
            }

            is MapIntent.NavigateToSelectedPropertyDetail -> {
                val property = _state.value.selectedMarkerProperty ?: return
                val userId = _state.value.currentUserId.orEmpty()
                _state.update { it.copy(selectedMarkerProperty = null) }
                _event.emit(MapEvent.NavigateToPropertyDetail(property.id, userId))
            }
        }
    }

    private fun subscribeSavedFilters() {
        getFilterPropertyUseCase()
            .distinctUntilChanged()
            .onEach { filters ->
                _state.update { it.copy(filtersProperty = filters) }
                recomputeFilteredMarkers()
            }
            .launchIn(viewModelScope)
    }

    private fun recomputeFilteredMarkers() {
        _state.update { current ->
            val rates = current.currencyRates
            val base = current.filtersProperty?.let { f ->
                filterPropertiesUseCase(
                    properties = current.markers,
                    filters = f,
                    rates = rates
                )
            } ?: current.markers

            val filtered = base.filter { marker ->
                val statusMatch =
                    current.selectedStatuses.isEmpty() || marker.status in current.selectedStatuses
                val typeMatch =
                    current.selectedTypes.isEmpty() || marker.type in current.selectedTypes
                statusMatch && typeMatch
            }

            current.copy(filteredMarkers = filtered)
        }
    }

    private fun observeLocation() {

        viewModelScope.launch {

            observeLocationUseCase().collect { location ->

                _state.update {
                    it.copy(
                        userLocation = UserLocation(
                            lat = location.latitude,
                            lon = location.longitude,
                            bearing = location.bearing
                        )
                    )
                }
            }
        }
    }

    private fun loadProperties() {
        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.id
            _state.update {
                it.copy(
                    currentUserId = userId,
                    isLoading = true
                )
            }

            val rates = when (val ratesResult = getTodayRatesUseCase()) {
                is Resource.Success -> ratesResult.data
                else -> emptyMap()
            }

            when (val result = getPropertiesUseCase(userId)) {

                is Resource.Success -> {
                    val catalog = result.data.forMainCatalogDisplay()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            markers = catalog,
                            currencyRates = rates
                        )
                    }
                    recomputeFilteredMarkers()
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception
                        )
                    }
                }
            }
        }
    }
}
