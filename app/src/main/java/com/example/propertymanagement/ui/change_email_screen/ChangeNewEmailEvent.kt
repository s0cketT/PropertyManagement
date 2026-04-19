package com.example.propertymanagement.ui.change_email_screen

sealed class ChangeNewEmailEvent {
    data class NavigateToEmailLinkInstructions(val email: String) : ChangeNewEmailEvent()
    object NavigateBackToSettings : ChangeNewEmailEvent()
}
