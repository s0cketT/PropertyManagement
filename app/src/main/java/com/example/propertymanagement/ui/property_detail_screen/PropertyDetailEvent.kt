package com.example.propertymanagement.ui.property_detail_screen

sealed interface PropertyDetailEvent {

    data object NavigateBack : PropertyDetailEvent

    data object ShowAuthRequired : PropertyDetailEvent

    data object ShowRegistrationRequiredForRequest : PropertyDetailEvent

    data object ShowOwnPropertyRequestNotAllowed : PropertyDetailEvent

    data object ApplicationSubmitted : PropertyDetailEvent

    data object ApplicationSubmitFailed : PropertyDetailEvent
}
