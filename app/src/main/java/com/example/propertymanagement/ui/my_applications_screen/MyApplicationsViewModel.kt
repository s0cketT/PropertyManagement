package com.example.propertymanagement.ui.my_applications_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetMyPropertyApplicationsUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyApplicationsViewModel(
    private val getMyPropertyApplicationsUseCase: GetMyPropertyApplicationsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MyApplicationsState())
    val state: StateFlow<MyApplicationsState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<MyApplicationsEvent>(viewModelScope)
    val event = _event.flow

    init {
        initUser()
    }

    fun processIntent(intent: MyApplicationsIntent) {
        when (intent) {
            MyApplicationsIntent.NavigateBack ->
                viewModelScope.launch { _event.emit(MyApplicationsEvent.NavigateBack) }
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
                        applications = emptyList(),
                        error = null,
                    )
                }
            } else {
                loadApplications(userId)
            }
        }
    }

    private fun loadApplications(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getMyPropertyApplicationsUseCase(userId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            applications = result.data,
                            error = null,
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            applications = emptyList(),
                            error = result.exception,
                        )
                    }
                }
            }
        }
    }
}
