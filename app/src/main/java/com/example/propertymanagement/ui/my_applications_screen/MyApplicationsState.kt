package com.example.propertymanagement.ui.my_applications_screen

import com.example.propertymanagement.domain.model.PropertyApplication

data class MyApplicationsState(
    val isLoading: Boolean = false,
    val applications: List<PropertyApplication> = emptyList(),
    val error: String? = null,
    val currentUserId: String? = null,
)
