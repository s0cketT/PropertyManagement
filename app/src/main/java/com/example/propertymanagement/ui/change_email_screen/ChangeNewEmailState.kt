package com.example.propertymanagement.ui.change_email_screen

import com.example.propertymanagement.ui.auth_screen.AuthError

data class ChangeNewEmailState(
    val newEmail: String = "",
    val emailError: AuthError? = null,
    val isLoading: Boolean = false
)
