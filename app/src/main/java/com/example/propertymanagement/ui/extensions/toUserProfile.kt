package com.example.propertymanagement.ui.extensions

import com.example.propertymanagement.domain.model.UserProfile
import com.google.gson.Gson
import com.google.gson.JsonObject

fun String.toUserProfile(): UserProfile {
    val gson = Gson()
    val obj = gson.fromJson(this, JsonObject::class.java)
    if (!obj.has("phone")) {
        obj.addProperty("phone", "")
    }
    return gson.fromJson(obj, UserProfile::class.java)
}