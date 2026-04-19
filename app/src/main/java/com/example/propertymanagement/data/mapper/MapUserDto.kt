package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.UserDto
import com.example.propertymanagement.domain.model.AuthUser
import com.example.propertymanagement.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        email = email
    )
}

fun UserDto.toAuthUser(): AuthUser {
    return AuthUser(
        id = id,
        email = email,
        newEmail = newEmail,
    )
}