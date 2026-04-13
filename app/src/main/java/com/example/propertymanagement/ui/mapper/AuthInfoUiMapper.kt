package com.example.propertymanagement.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.auth_screen.AuthInfo

@Composable
fun AuthInfo.asString(): String {
    return when (this) {
        AuthInfo.UserExists -> stringResource(R.string.user_exists)
    }
}