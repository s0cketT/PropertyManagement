package com.example.propertymanagement.ui.personal_info_screen

import com.example.propertymanagement.domain.model.UserProfile

data class PersonalInfoState(
    val user: UserProfile? = null,
    val isLoading: Boolean = false,
    val avatarBytes: ByteArray? = null,
    val isAvatarRemoved: Boolean = false
)
