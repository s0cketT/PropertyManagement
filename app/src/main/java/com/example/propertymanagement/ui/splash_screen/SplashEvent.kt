package com.example.propertymanagement.ui.splash_screen

sealed class SplashEvent {
    object NavigateToMain : SplashEvent()
    object NavigateToAuth : SplashEvent()
}