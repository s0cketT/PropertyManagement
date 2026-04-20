package com.example.propertymanagement.ui.my_ads_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.ModerationStatus
import com.example.propertymanagement.domain.model.MyAdsListingFilter
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetMyPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyAdsViewModel(
    private val getMyPropertiesUseCase: GetMyPropertiesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getTodayRatesUseCase: GetTodayRatesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyAdsState())
    val state: StateFlow<MyAdsState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<MyAdsEvent>(viewModelScope)
    val event = _event.flow

    init {
        initUser()
    }

    fun processIntent(intent: MyAdsIntent) {
        when (intent) {
            MyAdsIntent.NavigateBack ->
                viewModelScope.launch { _event.emit(MyAdsEvent.NavigateBack) }

            is MyAdsIntent.SelectListingFilter -> {
                _state.update { it.copy(listingFilter = intent.filter) }
                applyListingFilter()
            }

            is MyAdsIntent.OnPropertyClick -> onPropertyClick(intent.property)

            MyAdsIntent.DismissPropertyDetailSheet -> {
                _state.update { it.copy(detailSheetKey = null) }
            }
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
                        visibleList = emptyList(),
                        currencyRates = emptyMap(),
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
            val userId = _state.value.currentUserId ?: return@launch

            _state.update { it.copy(isLoading = true, error = null) }

            val rates = when (val ratesResult = getTodayRatesUseCase()) {
                is Resource.Success -> ratesResult.data
                else -> emptyMap()
            }

            when (val result = getMyPropertiesUseCase(userId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            properties = result.data,
                            currencyRates = rates
                        )
                    }
                    applyListingFilter()
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

    private fun applyListingFilter() {
        _state.update { s ->
            val filtered = filterByTab(s.properties, s.listingFilter)
            s.copy(visibleList = filtered)
        }
    }

    private fun onPropertyClick(property: Property) {
        val userId = _state.value.currentUserId
        if (userId == null) {
            viewModelScope.launch { _event.emit(MyAdsEvent.ShowAuthRequired) }
            return
        }
        _state.update {
            it.copy(
                detailSheetKey = MyAdPropertyDetailSheetKey(
                    propertyId = property.id,
                    userId = userId,
                ),
            )
        }
    }

    private fun filterByTab(
        properties: List<Property>,
        tab: MyAdsListingFilter
    ): List<Property> = when (tab) {
        MyAdsListingFilter.ALL -> properties
        MyAdsListingFilter.PUBLISHED ->
            properties.filter { it.moderationStatus == ModerationStatus.APPROVED }

        MyAdsListingFilter.PENDING ->
            properties.filter { it.moderationStatus == ModerationStatus.PENDING }

        MyAdsListingFilter.REJECTED ->
            properties.filter { it.moderationStatus == ModerationStatus.REJECTED }
    }
}
