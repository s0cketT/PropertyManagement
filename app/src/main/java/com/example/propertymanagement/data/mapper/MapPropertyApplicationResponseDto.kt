package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.PropertyApplicationResponseDto
import com.example.propertymanagement.domain.model.ApplicationStatus
import com.example.propertymanagement.domain.model.PropertyApplication
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private fun String?.parseApplicationCreatedAt(): Instant? {
    if (isNullOrBlank()) {
        return null
    }
    val value = trim()
    runCatching { Instant.parse(value) }.getOrNull()?.let { return it }
    runCatching {
        OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant()
    }.getOrNull()?.let { return it }
    runCatching {
        OffsetDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME).toInstant()
    }.getOrNull()?.let { return it }
    val normalized = if ('T' in value) value else value.replaceFirst(" ", "T")
    return runCatching {
        LocalDateTime.parse(normalized, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .toInstant(ZoneOffset.UTC)
    }.getOrNull()
}

fun PropertyApplicationResponseDto.toDomain(): PropertyApplication {
    return PropertyApplication(
        id = id,
        propertyId = property_id,
        propertyTitle = property_title,
        status = ApplicationStatus.fromDb(application_status, application_status_id),
        statusId = application_status_id,
        createdAt = created_at.parseApplicationCreatedAt(),
        managerName = manager_name?.trim()?.takeIf { it.isNotEmpty() },
        managerEmail = manager_email?.trim()?.takeIf { it.isNotEmpty() },
        managerPhone = manager_phone?.trim()?.takeIf { it.isNotEmpty() },
    )
}
