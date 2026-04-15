package com.example.propertymanagement.ui.common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Локализованная дата и время публикации (`properties.created_at`) для UI.
 */
fun formatPropertyPublicationTime(instant: Instant?, locale: Locale): String? {
    if (instant == null) return null
    val zoned = instant.atZone(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)
        .withLocale(locale)
    return formatter.format(zoned)
}
