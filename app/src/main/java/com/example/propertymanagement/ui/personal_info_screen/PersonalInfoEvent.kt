package com.example.propertymanagement.ui.personal_info_screen

sealed class PersonalInfoEvent {

    object NavigateBack : PersonalInfoEvent()

    object OpenGallery : PersonalInfoEvent()

    object SaveSuccess : PersonalInfoEvent()

    data class SaveError(val message: String? = null) : PersonalInfoEvent()
}