package com.example.propertymanagement.ui.auth_screen

sealed class AuthEvent {
    data class NavigateToCodeScreen(val email: String) : AuthEvent()
    object NavigateToLoginScreen : AuthEvent()
    object NavigateToMain : AuthEvent()
    object NavigateToRegisterScreen : AuthEvent()

    object ShowRegistrationSuccess : AuthEvent()

    object NavigateAsGuest : AuthEvent()
}