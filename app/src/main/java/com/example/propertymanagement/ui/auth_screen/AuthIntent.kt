package com.example.propertymanagement.ui.auth_screen

import com.example.propertymanagement.domain.model.SellerType

sealed class AuthIntent {

    data class FirstNameChanged(val value: String) : AuthIntent()
    data class EmailChanged(val email: String) : AuthIntent()
    data class AuthCheckChanged(val check: AuthCheck) : AuthIntent()
    data class PasswordChanged(val value: String) : AuthIntent()
    data class ConfirmPasswordChanged(val value: String) : AuthIntent()
    object Submit : AuthIntent()


    data class CodeChanged(val value: String) : AuthIntent()
    object VerifyOtp : AuthIntent()
    object SendOtp : AuthIntent()


    object Login : AuthIntent()
    object ToggleLoginMode : AuthIntent()

    object NavigateToLogin : AuthIntent()
    object NavigateToRegister : AuthIntent()

    object ContinueAsGuest : AuthIntent()

    data class SellerTypeChanged(val type: SellerType) : AuthIntent()

}
