package com.example.propertymanagement.ui.change_password_screen

import com.example.propertymanagement.ui.auth_screen.AuthError

data class SetNewPasswordState(
    val password: String = "",
    val confirmPassword: String = "",
    val passwordError: AuthError? = null,
    val confirmPasswordError: AuthError? = null,
    val isLoading: Boolean = false
)
