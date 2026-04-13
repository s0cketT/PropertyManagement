package com.example.propertymanagement.ui.extensions

import com.example.propertymanagement.domain.model.UserProfile
import com.google.gson.Gson


fun UserProfile.toJson(): String {
    return Gson().toJson(this)
}