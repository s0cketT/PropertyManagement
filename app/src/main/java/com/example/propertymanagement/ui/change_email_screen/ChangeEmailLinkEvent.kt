package com.example.propertymanagement.ui.change_email_screen

sealed class ChangeEmailLinkEvent {
    object Success : ChangeEmailLinkEvent()
    object NavigateBack : ChangeEmailLinkEvent()
}
