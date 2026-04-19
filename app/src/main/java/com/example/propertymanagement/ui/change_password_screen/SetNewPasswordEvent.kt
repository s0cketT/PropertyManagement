package com.example.propertymanagement.ui.change_password_screen

sealed class SetNewPasswordEvent {
    object NavigateBackToSettings : SetNewPasswordEvent()
    object Success : SetNewPasswordEvent()
}
