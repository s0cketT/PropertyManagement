package com.example.propertymanagement.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.propertymanagement.ui.theme.LocalAppDarkTheme
import com.yandex.mapkit.mapview.MapView

fun applyMapNightMode(
    mapView: MapView,
    nightModeEnabled: Boolean,
) {
    if (mapView.map.isNightModeEnabled == nightModeEnabled) {
        return
    }
    mapView.map.setNightModeEnabled(nightModeEnabled)
}

@Composable
fun ApplyMapNightModeEffect(mapView: MapView) {
    val nightModeEnabled = LocalAppDarkTheme.current
    LaunchedEffect(mapView, nightModeEnabled) {
        applyMapNightMode(
            mapView = mapView,
            nightModeEnabled = nightModeEnabled,
        )
    }
}
