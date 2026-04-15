package com.example.propertymanagement.ui.property_detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetPropertyDetailPricesUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.ToggleFavoriteUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PropertyDetailViewModel(
    private val propertyId: Int,
    private val userId: String,
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getTodayRatesUseCase: GetTodayRatesUseCase,
    private val getPropertyDetailPricesUseCase: GetPropertyDetailPricesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PropertyDetailState())
    val state: StateFlow<PropertyDetailState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<PropertyDetailEvent>(viewModelScope)
    val event = _event.flow

    init {
        loadProperty()
    }

    fun processIntent(intent: PropertyDetailIntent) {
        when (intent) {
            PropertyDetailIntent.NavigateBack ->
                viewModelScope.launch { _event.emit(PropertyDetailEvent.NavigateBack) }

            is PropertyDetailIntent.SetMapFullscreen ->
                _state.update { it.copy(isMapFullscreen = intent.open) }

            is PropertyDetailIntent.OpenImageViewer ->
                _state.update {
                    it.copy(
                        isImageViewerOpen = true,
                        imageViewerInitialPage = intent.pageIndex
                    )
                }

            PropertyDetailIntent.CloseImageViewer ->
                _state.update { it.copy(isImageViewerOpen = false) }

            PropertyDetailIntent.ToggleFavorite -> toggleFavorite()

            PropertyDetailIntent.SubmitRequest -> submitRequest()
        }
    }

    private fun submitRequest() {
        viewModelScope.launch {
            if (getCurrentUserUseCase()?.id == null) {
                _event.emit(PropertyDetailEvent.ShowRegistrationRequiredForRequest)
            }
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            val uid = getCurrentUserUseCase()?.id
            if (uid == null) {
                _event.emit(PropertyDetailEvent.ShowAuthRequired)
                return@launch
            }
            runCatching {
                toggleFavoriteUseCase(uid, propertyId)
            }.onSuccess {
                _state.update { s ->
                    val p = s.property ?: return@update s
                    s.copy(property = p.copy(isFavorite = !p.isFavorite))
                }
            }
        }
    }

    private fun loadProperty() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    notFound = false,
                    isMapFullscreen = false,
                    isImageViewerOpen = false,
                    convertedPrices = null
                )
            }
            val uid = userId.takeIf { it.isNotEmpty() }

            val rates = when (val ratesResult = getTodayRatesUseCase()) {
                is Resource.Success -> ratesResult.data
                else -> emptyMap()
            }

            when (val result = getPropertiesUseCase(uid)) {
                is Resource.Success -> {
                    val property = result.data.find { it.id == propertyId }
                    val converted = property?.let { p ->
                        getPropertyDetailPricesUseCase(p, rates)
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            property = property,
                            notFound = property == null,
                            convertedPrices = converted
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
}
