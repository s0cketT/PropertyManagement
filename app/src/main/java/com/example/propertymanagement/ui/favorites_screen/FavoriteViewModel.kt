package com.example.propertymanagement.ui.favorites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.visibleInPublicCatalog
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetManagerCommissionPercentUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.ToggleFavoriteUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getTodayRatesUseCase: GetTodayRatesUseCase,
    private val getManagerCommissionPercentUseCase: GetManagerCommissionPercentUseCase,
): ViewModel()
{

    private val _state = MutableStateFlow(FavoriteState())
    val state: StateFlow<FavoriteState> = _state

    private val _event = SingleFlowEvent<FavoriteEvent>(viewModelScope)
    val event = _event.flow

    init {
        initUser()
    }

    fun processIntent(intent: FavoriteIntent) {
        when (intent) {
            is FavoriteIntent.NavigateBack -> {
                viewModelScope.launch { _event.emit(FavoriteEvent.NavigateBack) }
            }
            is FavoriteIntent.NavigateToLogin -> {
                viewModelScope.launch { _event.emit(FavoriteEvent.NavigateToLogin) }
            }
            is FavoriteIntent.ToggleFavorite -> toggleFavorite(propertyId = intent.propertyId)
            is FavoriteIntent.OnPropertyClick -> onPropertyClick(property = intent.property)
        }
    }


    private fun initUser() {
        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.id
            _state.update { it.copy(currentUserId = userId) }
            if (userId == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        properties = emptyList(),
                        currencyRates = emptyMap(),
                        managerCommissionPercent = 0.0,
                        error = null
                    )
                }
            } else {
                loadProperties()
            }
        }
    }

    private fun loadProperties() {
        viewModelScope.launch {

            _state.update { it.copy(isLoading = true) }

            val userId = _state.value.currentUserId

            coroutineScope {
                val commissionDeferred = async { getManagerCommissionPercentUseCase() }

                val rates = when (val ratesResult = getTodayRatesUseCase()) {
                    is Resource.Success -> ratesResult.data
                    else -> emptyMap()
                }

                val commissionPercent = commissionDeferred.await()

                when (val result = getPropertiesUseCase(userId)) {
                    is Resource.Success -> {

                        val filtered = result.data
                            .filter { it.isFavorite }
                            .visibleInPublicCatalog()

                        _state.update {
                            it.copy(
                                isLoading = false,
                                properties = filtered,
                                currencyRates = rates,
                                managerCommissionPercent = commissionPercent,
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

    private fun toggleFavorite(propertyId: Int) {
        val userId = _state.value.currentUserId

        if (userId == null) {
            viewModelScope.launch {
                _event.emit(FavoriteEvent.ShowAuthRequired)
            }
            return
        }

        viewModelScope.launch {
            runCatching {
                toggleFavoriteUseCase(userId, propertyId)
            }.onSuccess {
                _state.update { current ->
                    val updated = current.properties.map { property ->
                        if (property.id == propertyId) {
                            property.copy(isFavorite = !property.isFavorite)
                        } else property
                    }
                    current.copy(
                        properties = updated
                            .filter { it.isFavorite }
                            .visibleInPublicCatalog()
                    )
                }
            }.onFailure {}
        }
    }

    private fun onPropertyClick(property: Property) {
        val userId = _state.value.currentUserId
        if (userId == null) {
            viewModelScope.launch {
                _event.emit(FavoriteEvent.ShowAuthRequired)
            }
            return
        }
        viewModelScope.launch {
            _event.emit(FavoriteEvent.NavigateToDetail(property = property, userId = userId))
        }
    }
}