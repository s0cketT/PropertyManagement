package com.example.propertymanagement.ui.publish_screen.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.propertymanagement.R
import com.example.propertymanagement.data.common.Constants
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.ui.components.ApplyMapNightModeEffect
import com.example.propertymanagement.ui.components.MapHelper
import com.example.propertymanagement.ui.map_screen.components.observeMapLifecycle
import com.example.propertymanagement.ui.theme.MapControlsIconOnDark
import com.example.propertymanagement.ui.theme.MapFullscreenControlScrim
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import org.koin.core.context.GlobalContext

@Composable
fun PublishMapPickerDialog(
    initialLatitude: Double?,
    initialLongitude: Double?,
    onDismiss: () -> Unit,
    onConfirm: (latitude: Double, longitude: Double) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val observeLocationUseCase = remember {
        GlobalContext.get().get<ObserveLocationUseCase>()
    }

    val mapView = remember { MapView(context) }
    val mapHelper = remember { MapHelper() }
    var selectedPoint by remember { mutableStateOf<Point?>(null) }
    var placemark by remember { mutableStateOf<PlacemarkMapObject?>(null) }

    ApplyMapNightModeEffect(mapView = mapView)

    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    var locationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, locationPermission) ==
                PackageManager.PERMISSION_GRANTED
        )
    }

    fun moveToDefaultRegion() {
        mapView.map.move(
            CameraPosition(
                Point(Constants.MAP_PICKER_FALLBACK_LAT, Constants.MAP_PICKER_FALLBACK_LON),
                Constants.MAP_PICKER_INITIAL_ZOOM,
                0f,
                0f
            ),
            Animation(Animation.Type.SMOOTH, 0.35f),
            null
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        locationPermissionGranted = granted
        if (!granted) {
            moveToDefaultRegion()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = observeMapLifecycle(lifecycleOwner, mapView)
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(mapView) {
        if (!locationPermissionGranted) {
            moveToDefaultRegion()
            permissionLauncher.launch(locationPermission)
        }
    }

    LaunchedEffect(initialLatitude, initialLongitude, mapView) {
        val lat = initialLatitude
        val lon = initialLongitude
        if (lat == null || lon == null) return@LaunchedEffect
        val point = Point(lat, lon)
        placemark?.let { mapView.map.mapObjects.remove(it) }
        val newPlacemark = mapView.map.mapObjects.addPlacemark(point)
        newPlacemark.setIcon(mapHelper.propertyMarkerIcon(context))
        placemark = newPlacemark
        selectedPoint = point
        mapView.map.move(
            CameraPosition(
                point,
                Constants.MAP_PICKER_SAVED_POINT_ZOOM,
                0f,
                0f
            ),
            Animation(Animation.Type.SMOOTH, 0.35f),
            null
        )
    }

    LaunchedEffect(mapView, locationPermissionGranted, initialLatitude, initialLongitude) {
        if (!locationPermissionGranted) return@LaunchedEffect
        val hasSavedPoint = initialLatitude != null && initialLongitude != null
        observeLocationUseCase().collect { location ->
            mapHelper.updateUserLocation(
                mapView,
                UserLocation(
                    lat = location.latitude,
                    lon = location.longitude,
                    bearing = location.bearing
                ),
                moveCameraOnFirstFix = !hasSavedPoint
            )
        }
    }

    DisposableEffect(mapView) {
        val listener = object : InputListener {
            override fun onMapTap(map: Map, point: Point) {
                placemark?.let { mapView.map.mapObjects.remove(it) }
                val newPlacemark = mapView.map.mapObjects.addPlacemark(point)
                newPlacemark.setIcon(mapHelper.propertyMarkerIcon(context))
                placemark = newPlacemark
                selectedPoint = point
            }

            override fun onMapLongTap(map: Map, point: Point) = Unit
        }
        mapView.map.addInputListener(listener)
        onDispose {
            mapView.map.removeInputListener(listener)
            placemark?.let { mapView.map.mapObjects.remove(it) }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapHelper.release(mapView)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                AndroidView(
                    factory = { mapView },
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(PaddingMedium)
                        .background(MapFullscreenControlScrim, RoundedCornerShape(PropertyDetailMapCornerRadius))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = MapControlsIconOnDark
                    )
                }

                if (selectedPoint != null) {
                    Button(
                        onClick = {
                            val p = selectedPoint ?: return@Button
                            onConfirm(p.latitude, p.longitude)
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(PaddingLarge)
                    ) {
                        Text(stringResource(R.string.publish_map_done))
                    }
                }
            }
        }
    }
}
