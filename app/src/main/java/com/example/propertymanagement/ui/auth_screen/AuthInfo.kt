package com.example.propertymanagement.ui.auth_screen

sealed class AuthInfo {
    object UserExists : AuthInfo()
}