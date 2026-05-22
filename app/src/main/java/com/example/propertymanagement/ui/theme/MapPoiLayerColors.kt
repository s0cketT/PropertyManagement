package com.example.propertymanagement.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.propertymanagement.domain.model.NearbyPoiCategory

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

    /** ARGB для [android.graphics.Paint]. */
    fun fillArgb(category: NearbyPoiCategory): Int =
        when (category) {
            NearbyPoiCategory.SCHOOL -> 0xFF7C3AED.toInt()
            NearbyPoiCategory.POLYCLINIC -> 0xFF0F766E.toInt()
            NearbyPoiCategory.GROCERY -> 0xFFB45309.toInt()
        }
}
