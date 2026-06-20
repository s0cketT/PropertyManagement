package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView

@Composable
internal fun PropertyDetailMapRouteOriginMapPickerEffect(
    mapView: MapView?,
    isActive: Boolean,
    onPointPicked: (Double, Double) -> Unit,
) {
    DisposableEffect(mapView, isActive) {
        val view = mapView
        if (view == null || !isActive) {
            onDispose { }
        } else {
            val listener = object : InputListener {
                override fun onMapTap(map: Map, point: Point) {
                    onPointPicked(point.latitude, point.longitude)
                }

                override fun onMapLongTap(map: Map, point: Point) = Unit
            }
            view.map.addInputListener(listener)
            onDispose {
                view.map.removeInputListener(listener)
            }
        }
    }
}
