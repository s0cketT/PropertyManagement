package com.example.propertymanagement.ui.publish_screen

import androidx.annotation.StringRes

sealed class PublishEvent {
    object NavigateBack : PublishEvent()
    object NavigateToCategorySelection : PublishEvent()

    object OpenGallery : PublishEvent()

    object ShowAuthRequired : PublishEvent()

    object ShowSaveFailedTryLater : PublishEvent()

    data class ShowValidationError(val message: String) : PublishEvent()

    data class ShowValidationErrorRes(@StringRes val messageRes: Int) : PublishEvent()
}