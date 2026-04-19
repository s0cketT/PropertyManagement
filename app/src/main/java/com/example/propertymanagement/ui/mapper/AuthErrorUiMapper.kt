package com.example.propertymanagement.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.auth_screen.AuthError

@Composable
fun AuthError.asString(): String {
    return when (this) {
        AuthError.InvalidEmail -> stringResource(R.string.error_invalid_email)
        AuthError.EmailAlreadyInUse -> stringResource(R.string.error_email_in_use)
        AuthError.PasswordEmpty -> stringResource(R.string.error_password_empty)
        AuthError.PasswordTooShort -> stringResource(R.string.error_password_short)
        AuthError.ConfirmPasswordEmpty -> stringResource(R.string.error_confirm_password_empty)
        AuthError.PasswordsNotMatch -> stringResource(R.string.error_passwords_not_match)
        AuthError.Unknown -> stringResource(R.string.error_unknown)
        AuthError.EmptyField -> stringResource(R.string.error_empty_field)
        AuthError.InvalidOtp -> stringResource(R.string.error_invalid_otp)
        AuthError.InvalidCredentials -> stringResource(R.string.error_invalid_credentials)
        AuthError.NewEmailSameAsCurrent -> stringResource(R.string.error_new_email_same_as_current)
    }
}