package com.example.propertymanagement.ui.change_password_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.UpdatePasswordUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.auth_screen.AuthError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetNewPasswordViewModel(
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SetNewPasswordState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<SetNewPasswordEvent>(viewModelScope)
    val event = _event.flow

    fun processIntent(intent: SetNewPasswordIntent) {
        when (intent) {
            is SetNewPasswordIntent.PasswordChanged -> {
                _state.update {
                    it.copy(password = intent.value, passwordError = null)
                }
            }
            is SetNewPasswordIntent.ConfirmPasswordChanged -> {
                _state.update {
                    it.copy(confirmPassword = intent.value, confirmPasswordError = null)
                }
            }
            SetNewPasswordIntent.Submit -> submit()
            SetNewPasswordIntent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(SetNewPasswordEvent.NavigateBackToSettings)
                }
            }
        }
    }

    private fun submit() {
        val s = _state.value
        val passwordError = validatePassword(s.password)
        val confirmError = validateConfirm(s.password, s.confirmPassword)
        if (passwordError != null || confirmError != null) {
            _state.update {
                it.copy(
                    passwordError = passwordError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            runCatching {
                updatePasswordUseCase(s.password)
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
                _event.emit(SetNewPasswordEvent.Success)
                _event.emit(SetNewPasswordEvent.NavigateBackToSettings)
            }.onFailure {
                _state.update {
                    it.copy(
                        isLoading = false,
                        passwordError = AuthError.Unknown
                    )
                }
            }
        }
    }

    private fun validatePassword(password: String): AuthError? {
        if (password.isBlank()) return AuthError.PasswordEmpty
        if (password.length < 6) return AuthError.PasswordTooShort
        return null
    }

    private fun validateConfirm(password: String, confirm: String): AuthError? {
        if (confirm.isBlank()) return AuthError.ConfirmPasswordEmpty
        if (password != confirm) return AuthError.PasswordsNotMatch
        return null
    }
}
