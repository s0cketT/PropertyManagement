package com.example.propertymanagement.ui.property_detail_screen.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.ui.components.MapHelper
import com.example.propertymanagement.ui.components.OsmMapPoiMarkersLayer
import com.example.propertymanagement.ui.map_screen.components.observeMapLifecycle
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map as YandexMap
import com.yandex.mapkit.mapview.MapView
import org.koin.core.context.GlobalContext

@Composable
internal fun PropertyDetailMapHost(
    property: Property,
    visibleMapPois: List<NearbyMapPoi>,
    modifier: Modifier = Modifier,
    trackUserLocation: Boolean = false,
    locationPermissionGranted: Boolean = false,
    onMapReady: ((MapView, MapHelper) -> Unit)? = null,
    onUserLocationUpdate: ((UserLocation) -> Unit)? = null,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }
    val mapHelper = remember { MapHelper() }
    val poiLayer = remember { OsmMapPoiMarkersLayer() }
    val observeLocationUseCase = remember {
        GlobalContext.get().get<ObserveLocationUseCase>()
    }

    val mapReadyCallback by rememberUpdatedState(onMapReady)
    LaunchedEffect(mapView, mapHelper) {
        mapReadyCallback?.invoke(mapView, mapHelper)
    }

    val userLocationCallback by rememberUpdatedState(onUserLocationUpdate)
    LaunchedEffect(trackUserLocation, locationPermissionGranted, mapView, mapHelper) {
        if (!trackUserLocation || !locationPermissionGranted) {
            return@LaunchedEffect
        }
        observeLocationUseCase().collect { location ->
            val userLoc = UserLocation(
                lat = location.latitude,
                lon = location.longitude,
                bearing = location.bearing,
            )
            mapHelper.updateUserLocation(
                mapView = mapView,
                location = userLoc,
                moveCameraOnFirstFix = false,
            )
            userLocationCallback?.invoke(userLoc)
        }
    }

    val poiZoomListener = remember(mapView, poiLayer) {
        object : CameraListener {
            override fun onCameraPositionChanged(
                map: YandexMap,
                cameraPosition: CameraPosition,
                cameraUpdateReason: CameraUpdateReason,
                finished: Boolean,
            ) {
                poiLayer.onZoomLevelChanged(mapView, cameraPosition.zoom)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = observeMapLifecycle(lifecycleOwner, mapView)
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            poiLayer.clear(mapView)
            mapHelper.release(mapView)
        }
    }

    DisposableEffect(mapView, poiZoomListener) {
        mapView.map.addCameraListener(poiZoomListener)
        onDispose {
            mapView.map.removeCameraListener(poiZoomListener)
        }
    }

    LaunchedEffect(
        property.id,
        property.latitude,
        property.longitude,
        visibleMapPois,
    ) {
        mapHelper.showSinglePropertyMarker(mapView, property)
        poiLayer.update(mapView, visibleMapPois) { poi ->
            val label = poiTapLabel(context = context, poi = poi)
            Toast.makeText(context, label, Toast.LENGTH_SHORT).show()
        }
        poiLayer.onZoomLevelChanged(mapView, mapView.map.cameraPosition.zoom)
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
    )
}

private fun poiTapLabel(context: Context, poi: NearbyMapPoi): String {
    val name = poi.name.trim()
    if (name.isNotEmpty()) {
        return name
    }
    val res = context.resources
    val id = poi.osmId
    return when (poi.category) {
        NearbyPoiCategory.SCHOOL ->
            res.getString(R.string.property_detail_poi_unnamed_school, id.toString())
        NearbyPoiCategory.POLYCLINIC ->
            res.getString(R.string.property_detail_poi_unnamed_clinic, id.toString())
        NearbyPoiCategory.GROCERY ->
            res.getString(R.string.property_detail_poi_unnamed_grocery, id.toString())
    }
}
