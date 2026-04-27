package com.example.propertymanagement.ui.filters_screen.city_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.GetCitiesByRegionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CitySelectionViewModel(
    private val regionId: Long,
    private val initialSelectedCityIds: Set<Long>,
    private val getCitiesByRegionUseCase: GetCitiesByRegionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        CitySelectionState(
            selectedCityIds = initialSelectedCityIds,
        )
    )
    val state: StateFlow<CitySelectionState> = _state

    init {
        loadCities()
    }

    fun onCityToggle(cityId: Long) {
        _state.update { current ->
            val next = if (cityId in current.selectedCityIds) {
                current.selectedCityIds - cityId
            } else {
                current.selectedCityIds + cityId
            }

            current.copy(selectedCityIds = next)
        }
    }

    fun retry() {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching {
                getCitiesByRegionUseCase(regionId = regionId)
            }.onSuccess { cities ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        cities = cities,
                    )
                }
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = throwable,
                    )
                }
            }
        }
    }
}

