package com.example.propertymanagement.ui.change_email_screen

sealed class ChangeNewEmailIntent {
    data class NewEmailChanged(val value: String) : ChangeNewEmailIntent()
    object Submit : ChangeNewEmailIntent()
    object NavigateBack : ChangeNewEmailIntent()
}
