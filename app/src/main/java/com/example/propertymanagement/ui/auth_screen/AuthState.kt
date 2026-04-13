package com.example.propertymanagement.ui.auth_screen

import com.example.propertymanagement.domain.model.SellerType

data class AuthState(
    val firstName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val code: String = "",

    val check: AuthCheck = AuthCheck.REGISTER,

    val sellerType: SellerType? = null,
    val sellerTypeError: AuthError? = null,

    val firstNameError: AuthError? = null,
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val confirmPasswordError: AuthError? = null,
    val otpError: AuthError? = null,
    val isOtpLogin: Boolean = false,

    val emailInfo: AuthInfo? = null,
    val isLoading: Boolean = false,
    val otpSent: Boolean = false,
    val isRegistered: Boolean = false
)

