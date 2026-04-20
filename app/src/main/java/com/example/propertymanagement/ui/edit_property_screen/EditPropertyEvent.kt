package com.example.propertymanagement.ui.edit_property_screen

import androidx.annotation.StringRes

sealed class EditPropertyEvent {
    object NavigateBack : EditPropertyEvent()
    object SaveSuccess : EditPropertyEvent()
    object NavigateToCategorySelection : EditPropertyEvent()

    object OpenGallery : EditPropertyEvent()

    object ShowAuthRequired : EditPropertyEvent()

    data class ShowValidationError(val message: String) : EditPropertyEvent()

    data class ShowValidationErrorRes(@StringRes val messageRes: Int) : EditPropertyEvent()
}
