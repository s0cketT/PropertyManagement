package com.example.propertymanagement.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import android.graphics.Color as AndroidColor

/**
 * Цвета меток POI на карте объявления (OSM). Приглушённые оттенки, чтобы не перегружать карту.
 */
object MapPoiLayerColors {

    fun composeColor(category: NearbyPoiCategory): Color =
        when (category) {
            NearbyPoiCategory.SCHOOL -> Color(0xFF7C3AED)
            NearbyPoiCategory.POLYCLINIC -> Color(0xFF0F766E)
            NearbyPoiCategory.GROCERY -> Color(0xFFB45309)
        }

    fun chipSelectedContainer(category: NearbyPoiCategory): Color =
        composeColor(category).copy(alpha = 0.14f)

    fun chipSelectedBorder(category: NearbyPoiCategory): Color =
        composeColor(category).copy(alpha = 0.45f)

    fun chipSelectedLabel(category: NearbyPoiCategory): Color =
        composeColor(category)

    fun overlayChipContainer(selected: Boolean): Color =
        if (selected) {
            Color.White.copy(alpha = 0.96f)
        } else {
            Color.White.copy(alpha = 0.82f)
        }

    fun overlayChipSelectedContainer(category: NearbyPoiCategory): Color =
        composeColor(category)

    fun markerBadgeBackgroundDayArgb(): Int = 0xFFFFFFFF.toInt()

    fun markerBadgeBackgroundNightArgb(category: NearbyPoiCategory): Int {
        val accent = composeColor(category)
        return AndroidColor.argb(
            255,
            (36 + accent.red * 255 * 0.18f).toInt().coerceIn(0, 255),
            (40 + accent.green * 255 * 0.18f).toInt().coerceIn(0, 255),
            (44 + accent.blue * 255 * 0.18f).toInt().coerceIn(0, 255),
        )
    }

    fun markerDotStrokeNightArgb(): Int = 0xFF6B7280.toInt()

    /** ARGB для [android.graphics.Paint]. */
    fun fillArgb(category: NearbyPoiCategory): Int =
        when (category) {
            NearbyPoiCategory.SCHOOL -> 0xFF7C3AED.toInt()
            NearbyPoiCategory.POLYCLINIC -> 0xFF0F766E.toInt()
            NearbyPoiCategory.GROCERY -> 0xFFB45309.toInt()
        }
}
