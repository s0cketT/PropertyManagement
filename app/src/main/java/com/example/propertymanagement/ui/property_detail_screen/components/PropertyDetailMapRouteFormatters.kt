package com.example.propertymanagement.ui.property_detail_screen.components

import com.yandex.mapkit.Time
import com.yandex.mapkit.geometry.PolylinePosition
import com.yandex.mapkit.transport.masstransit.Route
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

internal data class RouteDurationParts(
    val value: String,
    val unit: String?,
)

internal fun formatRouteDuration(seconds: Double): RouteDurationParts {
    val totalMinutes = (seconds / 60.0).roundToInt().coerceAtLeast(1)
    if (totalMinutes < 60) {
        return RouteDurationParts(
            value = totalMinutes.toString(),
            unit = "мин",
        )
    }

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return if (minutes == 0) {
        RouteDurationParts(
            value = hours.toString(),
            unit = "ч",
        )
    } else {
        RouteDurationParts(
            value = "$hours ч $minutes",
            unit = "мин",
        )
    }
}

internal fun formatRouteDistanceMeters(meters: Double): String {
    if (meters >= 1000.0) {
        val km = meters / 1000.0
        return if (km >= 10.0) {
            String.format(Locale.getDefault(), "%.0f км", km)
        } else {
            String.format(Locale.getDefault(), "%.1f км", km)
        }
    }
    return String.format(Locale.getDefault(), "%.0f м", meters)
}

internal fun Time.displayClockTime(): String? {
    val formatted = text?.trim().orEmpty()
    if (formatted.isNotEmpty()) return formatted
    if (value <= 0L) return null
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(value))
}

internal fun Route.masstransitDistanceText(mode: PropertyDetailMapRouteMode): String = when (mode) {
    PropertyDetailMapRouteMode.BICYCLE -> formatRouteDistanceMeters(totalDistanceMeters())
    PropertyDetailMapRouteMode.PEDESTRIAN,
    PropertyDetailMapRouteMode.TRANSIT,
    -> metadata.weight.walkingDistance.text
    PropertyDetailMapRouteMode.DRIVING -> error("driving uses drivingSummary")
}

private fun Route.totalDistanceMeters(): Double = runCatching {
    val segmentCount = geometry.points.size - 1
    if (segmentCount <= 0) return metadata.weight.walkingDistance.value
    distanceBetweenPolylinePositions(
        PolylinePosition(0, 0.0),
        PolylinePosition(segmentCount - 1, 1.0),
    )
}.getOrDefault(metadata.weight.walkingDistance.value)
