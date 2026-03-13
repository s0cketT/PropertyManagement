package com.example.propertymanagement.domain.common

sealed class Resource<out T, out E>(
    open val data: T? = null,
    open val exception: E? = null
) {
    data class Success<out T, out E>(override val data: T) : Resource<T, E>()

    data class Error<out T, out E>(override val exception: E) : Resource<T, E>()

}