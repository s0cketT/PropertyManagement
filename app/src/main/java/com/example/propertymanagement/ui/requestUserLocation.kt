package com.example.propertymanagement.ui

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

/**
 * Один раз подстраивает камеру под координаты пользователя, не добавляет метки и не очищает объекты карты.
 */
@SuppressLint("MissingPermission")
fun moveMapCameraToUserLocationOnce(
    fusedLocationClient: FusedLocationProviderClient,
    mapView: MapView,
    zoom: Float = 16f
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        2000L
    )
        .setMinUpdateIntervalMillis(1000L)
        .setMaxUpdates(1)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation ?: return
            val userPoint = Point(location.latitude, location.longitude)
            mapView.map.move(
                CameraPosition(
                    userPoint,
                    zoom,
                    0f,
                    0f
                ),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}

@SuppressLint("MissingPermission")
fun requestUserLocation(
    fusedLocationClient: FusedLocationProviderClient,
    mapView: MapView
) {

    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        2000L // интервал обновления
    )
        .setMinUpdateIntervalMillis(1000L)
        .setMaxUpdates(1) // ❗ Получаем только 1 обновление
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation ?: return

            val userPoint = Point(location.latitude, location.longitude)

            mapView.map.move(
                CameraPosition(
                    userPoint,
                    16f,
                    0f,
                    0f
                ),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )

            mapView.map.mapObjects.clear()
            mapView.map.mapObjects.addPlacemark(userPoint)

            // Останавливаем обновления после получения точки
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}
