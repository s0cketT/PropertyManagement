package com.example.propertymanagement.ui.change_email_screen

sealed class ChangeEmailLinkIntent {
    object CheckSynced : ChangeEmailLinkIntent()
    object NavigateBack : ChangeEmailLinkIntent()
}
