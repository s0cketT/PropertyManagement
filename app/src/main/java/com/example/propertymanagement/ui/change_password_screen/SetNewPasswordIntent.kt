package com.example.propertymanagement.ui.change_password_screen

sealed class SetNewPasswordIntent {
    data class PasswordChanged(val value: String) : SetNewPasswordIntent()
    data class ConfirmPasswordChanged(val value: String) : SetNewPasswordIntent()
    object Submit : SetNewPasswordIntent()
    object NavigateBack : SetNewPasswordIntent()
}
