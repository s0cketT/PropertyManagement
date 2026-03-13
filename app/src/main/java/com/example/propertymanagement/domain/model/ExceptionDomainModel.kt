package com.example.propertymanagement.domain.model

data class ExceptionDomainModel(
    val message: String,
    val originalException: Throwable? = null,
) {
    companion object {
        fun from(throwable: Throwable): ExceptionDomainModel =
            ExceptionDomainModel(
                message = throwable.message ?: "Неизвестная ошибка",
                originalException = throwable
            )
    }
}
