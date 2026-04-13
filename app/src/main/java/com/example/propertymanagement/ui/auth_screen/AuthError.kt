package com.example.propertymanagement.ui.auth_screen

sealed class AuthError {
    object EmptyField : AuthError()
    object EmailAlreadyInUse : AuthError()
    object InvalidEmail : AuthError()
    object PasswordEmpty : AuthError()
    object PasswordTooShort : AuthError()
    object ConfirmPasswordEmpty : AuthError()
    object PasswordsNotMatch : AuthError()
    object Unknown : AuthError()
    object InvalidOtp : AuthError()
}