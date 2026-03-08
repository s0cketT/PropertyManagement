package com.example.propertymanagement.ui.filters_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FiltersViewModel(): ViewModel() {
    private val _state = MutableStateFlow(FiltersState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<FiltersEvent>(viewModelScope)
    val event = _event.flow

    fun processIntent(intent: FiltersIntent) {
        when (intent) {
            FiltersIntent.NavigateBack -> {
                _event.emit(FiltersEvent.NavigateBack)
            }

            FiltersIntent.NavigateToCategorySelection -> {
                _event.emit(FiltersEvent.NavigateToCategorySelection)
            }
        }
    }
}