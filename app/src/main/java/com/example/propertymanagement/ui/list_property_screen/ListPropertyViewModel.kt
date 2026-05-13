package com.example.propertymanagement.ui.property

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.forMainCatalogDisplay
import com.example.propertymanagement.domain.use_case.FilterPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetFilterPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetManagerCommissionPercentUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.ToggleFavoriteUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.list_property_screen.ListPropertyEvent
import com.example.propertymanagement.ui.list_property_screen.ListPropertyIntent
import com.example.propertymanagement.ui.list_property_screen.ListPropertyState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ListPropertyViewModel(
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,

    private val getFilterPropertyUseCase: GetFilterPropertyUseCase,
    private val filterPropertiesUseCase: FilterPropertiesUseCase,
    private val getTodayRatesUseCase: GetTodayRatesUseCase,
    private val getManagerCommissionPercentUseCase: GetManagerCommissionPercentUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ListPropertyState())
    val state: StateFlow<ListPropertyState> = _state

    private val _event = SingleFlowEvent<ListPropertyEvent>(viewModelScope)
    val event = _event.flow

    init {
        initUser()
    }

    private fun loadSelectedMarker() {
        getFilterPropertyUseCase()
            .distinctUntilChanged()
            .onEach { filterProperty ->
                if (filterProperty == null) {
                    _state.update { current ->
                        recomputePropertiesFilter(current.copy(activeFilters = null))
                    }
                } else {
                    applyFilters(filterProperty)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun applyFilters(filters: FiltersProperty) {
        _state.update { current ->
            recomputePropertiesFilter(current.copy(activeFilters = filters))
        }
    }

    private fun recomputePropertiesFilter(state: ListPropertyState): ListPropertyState {
        val base = state.activeFilters?.let { f ->
            filterPropertiesUseCase(
                properties = state.properties,
                filters = f,
                rates = state.currencyRates
            )
        } ?: state.properties
        val q = state.searchQuery.trim().lowercase()
        val visible = if (q.isEmpty()) {
            base
        } else {
            base.filter { property ->
                val titleMatches = property.title.lowercase().contains(q)
                val descriptionMatches = property.description?.lowercase()?.contains(q) ?: false
                titleMatches || descriptionMatches
            }
        }
        return state.copy(propertiesFilter = visible)
    }

    private fun initUser() {
        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.id
            _state.update { it.copy(currentUserId = userId) }
            loadProperties()
            loadSelectedMarker()
        }
    }

    fun processIntent(intent: ListPropertyIntent) {
        when (intent) {
            is ListPropertyIntent.NavigateBack -> navigateBack()
            is ListPropertyIntent.LoadProperties -> loadProperties()
            is ListPropertyIntent.ToggleFavorite -> toggleFavorite(propertyId = intent.propertyId)
            is ListPropertyIntent.OnSearchChanged -> { onSearchChanged(query = intent.query) }
            is ListPropertyIntent.OnPropertyClick -> onPropertyClick(property = intent.property)
            is ListPropertyIntent.OnFilterClick -> { viewModelScope.launch { _event.emit(ListPropertyEvent.NavigateToFilterScreen) } }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _event.emit(ListPropertyEvent.NavigateBack)
        }
    }

    private fun onSearchChanged(query: String) {
        _state.update { current ->
            recomputePropertiesFilter(
                current.copy(searchQuery = query.trim().lowercase())
            )
        }
    }
    private fun onPropertyClick(property: Property) {
        val userId = _state.value.currentUserId.orEmpty()
        viewModelScope.launch {
            _event.emit(ListPropertyEvent.NavigateToDetail(property = property, userId = userId))
        }
    }
    private fun toggleFavorite(propertyId: Int) {
        val userId = _state.value.currentUserId

        if (userId == null) {
            viewModelScope.launch {
                _event.emit(ListPropertyEvent.ShowAuthRequired)
            }
            return
        }

        viewModelScope.launch {
            runCatching {
                toggleFavoriteUseCase(userId, propertyId)
            }.onSuccess {
                _state.update { current ->
                    val flip: (Property) -> Property = { property ->
                        if (property.id == propertyId) {
                            property.copy(isFavorite = !property.isFavorite)
                        } else property
                    }
                    recomputePropertiesFilter(
                        current.copy(properties = current.properties.map(flip))
                    )
                }
            }.onFailure {
                // ничего не меняем
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
                        val raw = result.data
                        val moderationCounts = raw.groupingBy { it.moderationStatus }.eachCount()
                        Log.d(
                            TAG,
                            "loadProperties success: userId=${userId ?: "null (guest)"} " +
                                "rawCount=${raw.size} moderationCounts=$moderationCounts",
                        )
                        val approvedOnly = raw.forMainCatalogDisplay()
                        Log.d(
                            TAG,
                            "loadProperties after forMainCatalogDisplay: count=${approvedOnly.size}",
                        )
                        val savedFilters = getFilterPropertyUseCase().first()
                        Log.d(
                            TAG,
                            "loadProperties savedFilters: present=${savedFilters != null} " +
                                "deal=${savedFilters?.selectedDealType} type=${savedFilters?.type} " +
                                "onlyWithPhotos=${savedFilters?.onlyWithPhotos ?: false}",
                        )
                        _state.update { current ->
                            recomputePropertiesFilter(
                                current.copy(
                                    isLoading = false,
                                    properties = approvedOnly,
                                    currencyRates = rates,
                                    managerCommissionPercent = commissionPercent,
                                    activeFilters = savedFilters
                                )
                            )
                        }
                        Log.d(
                            TAG,
                            "loadProperties UI list: properties.size=${_state.value.properties.size} " +
                                "propertiesFilter.size=${_state.value.propertiesFilter.size} " +
                                "searchQuery=${_state.value.searchQuery}",
                        )
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "loadProperties error: ${result.exception}")
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

    private companion object {
        private const val TAG = "PropertyCatalog"
    }
}