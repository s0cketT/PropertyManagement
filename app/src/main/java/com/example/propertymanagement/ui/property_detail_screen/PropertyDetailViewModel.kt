package com.example.propertymanagement.ui.property_detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetFilterPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetManagerCommissionPercentUseCase
import com.example.propertymanagement.domain.use_case.GetMyPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetNearbyMapPoisUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetPropertyDetailPricesUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.SubmitPropertyApplicationUseCase
import com.example.propertymanagement.domain.use_case.ToggleFavoriteUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.list_property_screen.catalogCardPriceLeadCurrency
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PropertyDetailViewModel(
    private val propertyId: Int,
    private val userId: String,
    private val useMyPropertiesForDetail: Boolean,
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val getMyPropertiesUseCase: GetMyPropertiesUseCase,
    private val getTodayRatesUseCase: GetTodayRatesUseCase,
    private val getPropertyDetailPricesUseCase: GetPropertyDetailPricesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val submitPropertyApplicationUseCase: SubmitPropertyApplicationUseCase,
    private val getManagerCommissionPercentUseCase: GetManagerCommissionPercentUseCase,
    private val getFilterPropertyUseCase: GetFilterPropertyUseCase,
    private val getNearbyMapPoisUseCase: GetNearbyMapPoisUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PropertyDetailState())
    val state: StateFlow<PropertyDetailState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<PropertyDetailEvent>(viewModelScope)
    val event = _event.flow

    init {
        if (!useMyPropertiesForDetail) {
            observeCatalogPriceLeadCurrency()
        }
        loadProperty()
    }

    private fun observeCatalogPriceLeadCurrency() {
        getFilterPropertyUseCase()
            .distinctUntilChanged()
            .onEach { filters ->
                _state.update {
                    it.copy(detailPriceLeadCurrency = catalogCardPriceLeadCurrency(filters))
                }
            }
            .launchIn(viewModelScope)
    }

    fun processIntent(intent: PropertyDetailIntent) {
        when (intent) {
            PropertyDetailIntent.NavigateBack ->
                viewModelScope.launch { _event.emit(PropertyDetailEvent.NavigateBack) }

            is PropertyDetailIntent.SetMapFullscreen ->
                _state.update { it.copy(isMapFullscreen = intent.open) }

            is PropertyDetailIntent.SetMapPoiCategoryVisible -> {
                val category = intent.category
                val visible = intent.visible
                _state.update { s ->
                    val next = s.visiblePoiCategories.toMutableSet()
                    if (visible) {
                        next.add(category)
                    } else {
                        next.remove(category)
                    }
                    s.copy(visiblePoiCategories = next)
                }
            }

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

            PropertyDetailIntent.SubmitRequest -> openApplicationSheet()

            PropertyDetailIntent.DismissApplicationSheet ->
                _state.update {
                    it.copy(
                        isApplicationSheetOpen = false,
                        applicationComment = ""
                    )
                }

            is PropertyDetailIntent.SetApplicationComment ->
                _state.update { it.copy(applicationComment = intent.text) }

            PropertyDetailIntent.ConfirmApplicationSubmit -> submitApplication()
        }
    }

    private fun openApplicationSheet() {
        viewModelScope.launch {
            val currentUserId = getCurrentUserUseCase()?.id
            if (currentUserId == null) {
                _event.emit(PropertyDetailEvent.ShowAuthRequired)
            } else if (_state.value.property?.ownerId == currentUserId) {
                _event.emit(PropertyDetailEvent.ShowOwnPropertyRequestNotAllowed)
            } else {
                _state.update {
                    it.copy(
                        isApplicationSheetOpen = true,
                        applicationComment = ""
                    )
                }
            }
        }
    }

    private fun submitApplication() {
        viewModelScope.launch {
            val uid = getCurrentUserUseCase()?.id
            if (uid == null) {
                _event.emit(PropertyDetailEvent.ShowAuthRequired)
                return@launch
            }
            if (_state.value.property?.ownerId == uid) {
                _event.emit(PropertyDetailEvent.ShowOwnPropertyRequestNotAllowed)
                return@launch
            }
            if (_state.value.isSubmittingApplication) return@launch

            _state.update { it.copy(isSubmittingApplication = true) }
            val comment = _state.value.applicationComment
            runCatching {
                submitPropertyApplicationUseCase(
                    propertyId = propertyId,
                    applicantUserId = uid,
                    comment = comment
                )
            }
                .onSuccess {
                    _state.update {
                        it.copy(
                            isApplicationSheetOpen = false,
                            applicationComment = "",
                            isSubmittingApplication = false
                        )
                    }
                    _event.emit(PropertyDetailEvent.ApplicationSubmitted)
                }
                .onFailure {
                    _state.update { it.copy(isSubmittingApplication = false) }
                    _event.emit(PropertyDetailEvent.ApplicationSubmitFailed)
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
                    convertedPrices = null,
                    currencyRates = emptyMap(),
                    detailPriceLeadCurrency = CurrencyType.USD,
                    managerCommissionPercent = 0.0,
                    isApplicationSheetOpen = false,
                    applicationComment = "",
                    isSubmittingApplication = false,
                    nearbyMapPois = emptyList(),
                    visiblePoiCategories = emptySet(),
                    isNearbyPoisLoading = false,
                    nearbyPoisLoadFailed = false,
                )
            }
            val uid = userId.takeIf { it.isNotEmpty() }

            coroutineScope {
                val commissionDeferred = async {
                    if (useMyPropertiesForDetail) {
                        0.0
                    } else {
                        getManagerCommissionPercentUseCase()
                    }
                }

                val rates = when (val ratesResult = getTodayRatesUseCase()) {
                    is Resource.Success -> ratesResult.data
                    else -> emptyMap()
                }

                val commissionPercent = commissionDeferred.await()

                if (useMyPropertiesForDetail) {
                    val ownerId = uid
                    if (ownerId == null) {
                        _state.update {
                            it.copy(isLoading = false, notFound = true)
                        }
                        return@coroutineScope
                    }
                    when (val result = getMyPropertiesUseCase(ownerId)) {
                        is Resource.Success -> {
                            val property = result.data.find { it.id == propertyId }
                            val converted = property?.let { p ->
                                getPropertyDetailPricesUseCase(
                                    property = p,
                                    rates = rates,
                                    managerCommissionPercent = 0.0,
                                )
                            }
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    property = property,
                                    notFound = property == null,
                                    convertedPrices = converted,
                                    currencyRates = rates,
                                    managerCommissionPercent = 0.0,
                                )
                            }
                            if (property != null) {
                                fetchNearbyMapPois(
                                    latitude = property.latitude,
                                    longitude = property.longitude,
                                )
                            }
                        }

                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception,
                                )
                            }
                        }
                    }
                } else {
                    when (val result = getPropertiesUseCase(uid)) {
                        is Resource.Success -> {
                            val catalog = result.data
                            val property = catalog.find { it.id == propertyId }
                            val converted = property?.let { p ->
                                getPropertyDetailPricesUseCase(
                                    property = p,
                                    rates = rates,
                                    managerCommissionPercent = commissionPercent,
                                )
                            }
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    property = property,
                                    notFound = property == null,
                                    convertedPrices = converted,
                                    currencyRates = rates,
                                    managerCommissionPercent = commissionPercent,
                                )
                            }
                            if (property != null) {
                                fetchNearbyMapPois(
                                    latitude = property.latitude,
                                    longitude = property.longitude,
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
    }

    private fun fetchNearbyMapPois(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isNearbyPoisLoading = true,
                    nearbyPoisLoadFailed = false,
                )
            }
            when (
                val result = getNearbyMapPoisUseCase(
                    latitude = latitude,
                    longitude = longitude,
                    radiusMeters = GetNearbyMapPoisUseCase.DEFAULT_RADIUS_METERS,
                )
            ) {
                is Resource.Success ->
                    _state.update {
                        it.copy(
                            nearbyMapPois = result.data,
                            isNearbyPoisLoading = false,
                            nearbyPoisLoadFailed = false,
                        )
                    }

                is Resource.Error ->
                    _state.update {
                        it.copy(
                            nearbyMapPois = emptyList(),
                            isNearbyPoisLoading = false,
                            nearbyPoisLoadFailed = true,
                        )
                    }
            }
        }
    }
}
