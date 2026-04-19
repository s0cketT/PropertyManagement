package com.example.propertymanagement.ui.change_email_screen

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.CheckUserExistsUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.RequestEmailChangeUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.auth_screen.AuthError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangeNewEmailViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkUserExistsUseCase: CheckUserExistsUseCase,
    private val requestEmailChangeUseCase: RequestEmailChangeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChangeNewEmailState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<ChangeNewEmailEvent>(viewModelScope)
    val event = _event.flow

    fun processIntent(intent: ChangeNewEmailIntent) {
        when (intent) {
            is ChangeNewEmailIntent.NewEmailChanged -> {
                _state.update {
                    it.copy(newEmail = intent.value, emailError = null)
                }
            }
            ChangeNewEmailIntent.Submit -> submit()
            ChangeNewEmailIntent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(ChangeNewEmailEvent.NavigateBackToSettings)
                }
            }
        }
    }

    private fun submit() {
        val email = _state.value.newEmail.trim()
        val err = validateEmail(email)
        if (err != null) {
            _state.update { it.copy(emailError = err) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, emailError = null) }
            val currentEmail = getCurrentUserUseCase()?.email?.trim()?.lowercase().orEmpty()
            if (currentEmail.isNotEmpty() && email.lowercase() == currentEmail) {
                _state.update {
                    it.copy(isLoading = false, emailError = AuthError.NewEmailSameAsCurrent)
                }
                return@launch
            }
            if (checkUserExistsUseCase(email)) {
                _state.update {
                    it.copy(isLoading = false, emailError = AuthError.EmailAlreadyInUse)
                }
                return@launch
            }
            runCatching {
                requestEmailChangeUseCase(email)
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
                _event.emit(ChangeNewEmailEvent.NavigateToEmailLinkInstructions(email))
            }.onFailure {
                _state.update {
                    it.copy(isLoading = false, emailError = AuthError.Unknown)
                }
            }
        }
    }

    private fun validateEmail(email: String): AuthError? {
        if (email.isBlank()) return AuthError.EmptyField
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return AuthError.InvalidEmail
        return null
    }

}
