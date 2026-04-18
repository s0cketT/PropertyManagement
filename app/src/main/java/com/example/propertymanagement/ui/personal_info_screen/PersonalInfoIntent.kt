package com.example.propertymanagement.ui.personal_info_screen

import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.UserProfile

sealed class PersonalInfoIntent {
    data class SetUser(val user: UserProfile) : PersonalInfoIntent()
    object OnBackClick : PersonalInfoIntent()

    object OnAvatarClick : PersonalInfoIntent()
    data class AvatarSelected(val image: ByteArray) : PersonalInfoIntent()

    object RemoveAvatar : PersonalInfoIntent()

    data class NameChanged(val value: String) : PersonalInfoIntent()
    data class PhoneNationalChanged(val value: String) : PersonalInfoIntent()
    data class SellerTypeChanged(val type: SellerType) : PersonalInfoIntent()

    object Save : PersonalInfoIntent()
}