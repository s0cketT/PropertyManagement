package com.example.propertymanagement.ui.extensions

import com.example.propertymanagement.domain.model.UserProfile
import com.google.gson.Gson

fun String.toUserProfile(): UserProfile {
    return Gson().fromJson(this, UserProfile::class.java)
}