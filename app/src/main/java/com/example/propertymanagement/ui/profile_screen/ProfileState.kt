package com.example.propertymanagement.ui.profile_screen

import com.example.propertymanagement.domain.model.UserProfile

data class ProfileState(
    val user: UserProfile? = null,
    val isLoading: Boolean = false,
)
