package com.example.propertymanagement.ui.filters_screen.region_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.GetRegionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegionSelectionViewModel(
    private val getRegionsUseCase: GetRegionsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RegionSelectionState())
    val state: StateFlow<RegionSelectionState> = _state

    init {
        loadRegions()
    }

    fun retry() {
        loadRegions()
    }

    private fun loadRegions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching {
                getRegionsUseCase()
            }.onSuccess { regions ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        regions = regions,
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

