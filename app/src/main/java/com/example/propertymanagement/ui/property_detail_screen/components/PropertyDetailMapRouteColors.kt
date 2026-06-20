package com.example.propertymanagement.ui.property_detail_screen.components

import kotlin.math.roundToInt

internal object PropertyDetailMapRouteColors {

    fun outline(isDarkTheme: Boolean): Int = if (isDarkTheme) {
        0xE6FFFFFF.toInt()
    } else {
        0xFF111827.toInt()
    }

    fun pedestrianStroke(isDarkTheme: Boolean): Int = if (isDarkTheme) {
        0xFFC4B5FD.toInt()
    } else {
        0xFF7C3AED.toInt()
    }

    fun drivingStroke(isDarkTheme: Boolean): Int = if (isDarkTheme) {
        0xFF60A5FA.toInt()
    } else {
        0xFF2563EB.toInt()
    }

    fun bicycleStroke(isDarkTheme: Boolean): Int = if (isDarkTheme) {
        0xFF34D399.toInt()
    } else {
        0xFF059669.toInt()
    }

    fun transitDefaultStroke(isDarkTheme: Boolean): Int = if (isDarkTheme) {
        0xFF5BD4CB.toInt()
    } else {
        0xFF0D5C56.toInt()
    }

    fun walkSegmentStroke(isDarkTheme: Boolean): Int = if (isDarkTheme) {
        0xFFE5E7EB.toInt()
    } else {
        0xFF6B7280.toInt()
    }

    fun strokeWidth(isDarkTheme: Boolean): Float = if (isDarkTheme) 2f else 1.5f

    fun outlineWidth(isDarkTheme: Boolean): Float = if (isDarkTheme) 1f else 0.5f

    fun lineColor(isDarkTheme: Boolean, overrideColor: Int?, fallbackResColor: Int): Int {
        val base = overrideColor ?: fallbackResColor
        return if (isDarkTheme) ensureVisibleOnDarkMap(base) else base
    }

    private fun ensureVisibleOnDarkMap(color: Int): Int {
        val opaque = if (color and 0xFF000000.toInt() != 0) color else color or 0xFF000000.toInt()
        val red = (opaque shr 16) and 0xFF
        val green = (opaque shr 8) and 0xFF
        val blue = opaque and 0xFF
        val luminance = 0.299 * red + 0.587 * green + 0.114 * blue
        if (luminance >= 110) return opaque

        fun boost(channel: Int): Int =
            (channel + (255 - channel) * 0.5).roundToInt().coerceIn(0, 255)

        return (0xFF shl 24) or (boost(red) shl 16) or (boost(green) shl 8) or boost(blue)
    }
}
