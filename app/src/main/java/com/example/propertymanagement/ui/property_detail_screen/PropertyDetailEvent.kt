package com.example.propertymanagement.ui.property_detail_screen

sealed interface PropertyDetailEvent {

    data object NavigateBack : PropertyDetailEvent

    data object ShowAuthRequired : PropertyDetailEvent

    /** Нужна регистрация, чтобы оставить заявку по объявлению. */
    data object ShowRegistrationRequiredForRequest : PropertyDetailEvent
}
