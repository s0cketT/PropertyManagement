package com.example.propertymanagement.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.model.PropertyMarker
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.domain.use_case.GetMarkersUseCase
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.map_screen.MapEvent
import com.example.propertymanagement.ui.map_screen.MapIntent
import com.example.propertymanagement.ui.map_screen.MapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.domain.model.PropertyType

class MapViewModel(
    private val observeLocationUseCase: ObserveLocationUseCase,
    private val getMarkersUseCase: GetMarkersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<MapEvent>(viewModelScope)
    val event = _event.flow

    init {
        loadMarkers()
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
                        // если null → полный сброс
                        null -> emptySet()

                        // если уже выбран → убираем (toggle)
                        else -> if (intent.status in current.selectedStatuses) {
                            current.selectedStatuses - intent.status
                        } else {
                            // добавляем к уже выбранным
                            current.selectedStatuses + intent.status
                        }
                    }

                    val filtered = if (newSelected.isEmpty()) {
                        current.markers  // ничего не выбрано → все метки
                    } else {
                        current.markers.filter { it.status in newSelected }
                    }
                    Log.d("!!!", newSelected.toString())
                    Log.d("!!!", filtered.size.toString())
                    current.copy(
                        selectedStatuses = newSelected,
                        filteredMarkers = filtered
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
    private fun loadMarkers() {

        viewModelScope.launch {

            val markers = getMarkersUseCase()

            _state.update {
                it.copy(
                    markers = markers,
                    filteredMarkers = markers
                )
            }
        }
    }

    private fun computeFilteredMarkers(
        markers: List<PropertyMarker>,
        selectedStatuses: Set<PropertyStatus>,
        selectedTypes: Set<PropertyType>
    ): List<PropertyMarker> {
        return markers.filter { marker ->
            val statusMatch = selectedStatuses.isEmpty() || marker.status in selectedStatuses
            val typeMatch = selectedTypes.isEmpty() || marker.type in selectedTypes
            statusMatch && typeMatch
        }
    }
}

