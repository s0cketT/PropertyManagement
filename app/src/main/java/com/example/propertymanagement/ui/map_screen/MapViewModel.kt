package com.example.propertymanagement.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.map_screen.MapEvent
import com.example.propertymanagement.ui.map_screen.MapIntent
import com.example.propertymanagement.ui.map_screen.MapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val observeLocationUseCase: ObserveLocationUseCase,
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<MapEvent>(viewModelScope)
    val event = _event.flow

    init {
        loadProperties()
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

                    current.copy(
                        selectedStatuses = newSelected,
                        filteredMarkers = computeFilteredMarkers(
                            markers = current.markers,
                            selectedStatuses = newSelected,
                            selectedTypes = current.selectedTypes
                        )
                    )
                }
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
                        filteredMarkers = computeFilteredMarkers(
                            markers = current.markers,
                            selectedStatuses = current.selectedStatuses,
                            selectedTypes = newSelectedTypes
                        )
                    )
                }
            }

            is MapIntent.NavigateFilterScreen -> {
                _event.emit(MapEvent.NavigateFilterScreen)
            }
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

            when (val result = getPropertiesUseCase(userId)) {

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            markers = result.data,
                            filteredMarkers = result.data
                        )
                    }
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

    private fun computeFilteredMarkers(
        markers: List<Property>,
        selectedStatuses: Set<PropertyStatus>,
        selectedTypes: Set<PropertyType>
    ): List<Property> {
        return markers.filter { marker ->
            val statusMatch = selectedStatuses.isEmpty() || marker.status in selectedStatuses
            val typeMatch = selectedTypes.isEmpty() || marker.type in selectedTypes
            statusMatch && typeMatch
        }
    }
}

