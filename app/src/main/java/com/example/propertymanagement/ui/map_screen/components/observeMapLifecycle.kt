package com.example.propertymanagement.ui.map_screen.components

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView

fun observeMapLifecycle(
    lifecycleOwner: LifecycleOwner,
    mapView: MapView
): DefaultLifecycleObserver {

    return object : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            MapKitFactory.getInstance().onStart()
            mapView.onStart()
        }

        override fun onStop(owner: LifecycleOwner) {
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }
}