package com.example.propertymanagement.ui.change_email_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.SyncProfileEmailIfAuthMatchesUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangeEmailLinkViewModel(
    private val expectedNewEmail: String,
    private val syncProfileEmailIfAuthMatchesUseCase: SyncProfileEmailIfAuthMatchesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChangeEmailLinkState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<ChangeEmailLinkEvent>(viewModelScope)
    val event = _event.flow

    fun processIntent(intent: ChangeEmailLinkIntent) {
        when (intent) {
            ChangeEmailLinkIntent.CheckSynced -> checkSynced()
            ChangeEmailLinkIntent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(ChangeEmailLinkEvent.NavigateBack)
                }
            }
        }
    }

    private fun checkSynced() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, showNotConfirmedError = false)
            }
            val result = syncProfileEmailIfAuthMatchesUseCase(expectedNewEmail)
            if (result.isSuccess) {
                _state.update { it.copy(isLoading = false) }
                _event.emit(ChangeEmailLinkEvent.Success)
            } else {
                _state.update {
                    it.copy(isLoading = false, showNotConfirmedError = true)
                }
            }
        }
    }
}
