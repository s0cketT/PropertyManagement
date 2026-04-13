package com.example.propertymanagement.ui.publish_screen

sealed class PublishEvent {
    object NavigateBack : PublishEvent()
    object NavigateToCategorySelection : PublishEvent()

    object OpenGallery : PublishEvent()

    object ShowAuthRequired : PublishEvent()

    data class ShowValidationError(val message: String) : PublishEvent()
}