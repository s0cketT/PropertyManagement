package com.example.propertymanagement.ui.property_detail_screen.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.ui.components.MapHelper
import com.example.propertymanagement.ui.theme.CardElevationLow
import com.example.propertymanagement.ui.theme.IconMedium
import com.example.propertymanagement.ui.theme.MapControlsIconOnDark
import com.example.propertymanagement.ui.theme.MapFullscreenControlScrim
import com.example.propertymanagement.ui.theme.LocalAppDarkTheme
import com.example.propertymanagement.ui.theme.MapRouteFullscreenBottomSheetInset
import com.example.propertymanagement.ui.theme.MapPreviewGradientEnd
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.example.propertymanagement.ui.theme.PropertyDetailMapPreviewHeight
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.yandex.mapkit.mapview.MapView

@Composable
fun PropertyDetailMapSection(
    property: Property,
    nearbyMapPois: List<NearbyMapPoi>,
    visiblePoiCategories: Set<NearbyPoiCategory>,
    onSetPoiCategoryVisible: (NearbyPoiCategory, Boolean) -> Unit,
    isNearbyPoisLoading: Boolean,
    nearbyPoisLoadFailed: Boolean,
    onOpenFullscreen: () -> Unit,
) {
    val visiblePois = remember(nearbyMapPois, visiblePoiCategories) {
        nearbyMapPois.filter { it.category in visiblePoiCategories }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(PropertyDetailMapPreviewHeight)
                .clip(RoundedCornerShape(PropertyDetailMapCornerRadius)),
            shape = RoundedCornerShape(PropertyDetailMapCornerRadius),
            tonalElevation = CardElevationLow,
            shadowElevation = CardElevationLow,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                PropertyDetailMapHost(
                    property = property,
                    visibleMapPois = visiblePois,
                    modifier = Modifier.fillMaxSize(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MapPreviewGradientEnd,
                                ),
                            ),
                        ),
                )

                IconButton(
                    onClick = onOpenFullscreen,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = PaddingMedium, end = PaddingMedium)
                        .background(
                            MapFullscreenControlScrim,
                            RoundedCornerShape(PropertyDetailMapCornerRadius),
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Fullscreen,
                        contentDescription = stringResource(R.string.property_detail_map_fullscreen_cd),
                        tint = MapControlsIconOnDark,
                        modifier = Modifier.size(IconMedium),
                    )
                }
            }
        }

        PropertyDetailMapPoiLayersPanel(
            visibleCategories = visiblePoiCategories,
            onSetCategoryVisible = onSetPoiCategoryVisible,
            nearbyMapPois = nearbyMapPois,
            visiblePois = visiblePois,
            isNearbyPoisLoading = isNearbyPoisLoading,
            nearbyPoisLoadFailed = nearbyPoisLoadFailed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SpacerMedium),
            style = MapPoiLayerPanelStyle.Default,
        )
    }
}

@Composable
fun PropertyDetailFullscreenMapDialog(
    property: Property,
    nearbyMapPois: List<NearbyMapPoi>,
    visiblePoiCategories: Set<NearbyPoiCategory>,
    onSetPoiCategoryVisible: (NearbyPoiCategory, Boolean) -> Unit,
    isNearbyPoisLoading: Boolean,
    nearbyPoisLoadFailed: Boolean,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    var locationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, locationPermission) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        locationPermissionGranted = granted
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, locationPermission) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(locationPermission)
        }
    }

    var mapRefs by remember { mutableStateOf<Pair<MapView, MapHelper>?>(null) }
    var userLocationState by remember { mutableStateOf<UserLocation?>(null) }
    val routeController = remember { PropertyDetailMapRouteController() }
    val routeOriginOverlay = remember { PropertyDetailMapRouteOriginOverlay() }
    var isRouteActive by remember { mutableStateOf(false) }
    var isRouteSheetVisible by remember { mutableStateOf(true) }
    var isPoiLayersActive by remember { mutableStateOf(false) }
    var isPoiLayersSheetVisible by remember { mutableStateOf(true) }
    var selectedRouteMode by remember { mutableStateOf(PropertyDetailMapRouteMode.PEDESTRIAN) }
    var isRouteLoading by remember { mutableStateOf(false) }
    var routeSummary by remember { mutableStateOf<PropertyDetailMapRouteSummary?>(null) }
    var routeOriginSource by remember {
        mutableStateOf(PropertyDetailMapRouteOriginSource.MY_LOCATION)
    }
    var myLocationRouteOrigin by remember { mutableStateOf<UserLocation?>(null) }
    var customRouteOrigin by remember { mutableStateOf<UserLocation?>(null) }
    var isPickingRouteOrigin by remember { mutableStateOf(false) }
    val isDarkTheme = LocalAppDarkTheme.current
    val routeErrorMessage = stringResource(R.string.property_detail_map_route_error)
    val noLocationMessage = stringResource(R.string.property_detail_map_route_no_location)

    val routeOrigin = resolveRouteOrigin(
        source = routeOriginSource,
        myLocationOrigin = myLocationRouteOrigin,
        customOrigin = customRouteOrigin,
        liveUserLocation = userLocationState,
    )
    val hasRouteOrigin = routeOrigin != null

    fun openPoiLayersPanel() {
        isPoiLayersActive = true
        isPoiLayersSheetVisible = true
    }

    fun minimizePoiLayersSheet() {
        isPoiLayersSheetVisible = false
    }

    fun restorePoiLayersSheet() {
        isPoiLayersSheetVisible = true
    }

    fun closePoiLayersPanel() {
        isPoiLayersActive = false
        isPoiLayersSheetVisible = true
    }

    fun openRoutePanel() {
        isRouteActive = true
        isRouteSheetVisible = true
        if (myLocationRouteOrigin == null) {
            myLocationRouteOrigin = userLocationState
        }
    }

    fun useMyLocationForRoute() {
        isPickingRouteOrigin = false
        routeOriginSource = PropertyDetailMapRouteOriginSource.MY_LOCATION
        val origin = userLocationState ?: run {
            Toast.makeText(context, noLocationMessage, Toast.LENGTH_SHORT).show()
            return
        }
        myLocationRouteOrigin = origin
        routeSummary = null
        mapRefs?.first?.let { routeOriginOverlay.clear(it) }
        val mapView = mapRefs?.first ?: return
        requestPropertyDetailMapRoute(
            mapView = mapView,
            routeController = routeController,
            property = property,
            selectedMode = selectedRouteMode,
            isDarkTheme = isDarkTheme,
            origin = origin,
            routeErrorMessage = routeErrorMessage,
            onLoading = { isRouteLoading = true },
            onSuccess = { summary ->
                isRouteLoading = false
                routeSummary = summary
            },
            onFailure = {
                isRouteLoading = false
                routeSummary = null
            },
        )
    }

    fun startPickRouteOriginOnMap() {
        routeOriginSource = PropertyDetailMapRouteOriginSource.CUSTOM
        isPickingRouteOrigin = true
        isRouteSheetVisible = false
        routeSummary = null
        isRouteLoading = false
        mapRefs?.first?.let { routeController.clearRoute(it) }
    }

    fun cancelPickRouteOrigin() {
        isPickingRouteOrigin = false
        isRouteSheetVisible = true
        if (customRouteOrigin == null) {
            routeOriginSource = PropertyDetailMapRouteOriginSource.MY_LOCATION
        }
        if (routeSummary == null && routeOrigin != null) {
            val mapView = mapRefs?.first ?: return
            val origin = routeOrigin ?: return
            requestPropertyDetailMapRoute(
                mapView = mapView,
                routeController = routeController,
                property = property,
                selectedMode = selectedRouteMode,
                isDarkTheme = isDarkTheme,
                origin = origin,
                routeErrorMessage = routeErrorMessage,
                onLoading = { isRouteLoading = true },
                onSuccess = { summary ->
                    isRouteLoading = false
                    routeSummary = summary
                },
                onFailure = {
                    isRouteLoading = false
                    routeSummary = null
                },
            )
        }
    }

    fun onCustomRouteOriginPicked(lat: Double, lon: Double) {
        customRouteOrigin = UserLocation(lat = lat, lon = lon, bearing = 0f)
        routeOriginSource = PropertyDetailMapRouteOriginSource.CUSTOM
        isPickingRouteOrigin = false
        isRouteSheetVisible = true
        routeSummary = null
        isRouteLoading = true
    }

    fun minimizeRouteSheet() {
        isRouteSheetVisible = false
    }

    fun restoreRouteSheet() {
        isRouteSheetVisible = true
    }

    fun closeRoutePanel() {
        mapRefs?.first?.let { mapView ->
            routeController.clearRoute(mapView)
            routeOriginOverlay.clear(mapView)
        }
        routeSummary = null
        isRouteLoading = false
        isRouteActive = false
        isRouteSheetVisible = true
        isPickingRouteOrigin = false
        routeOriginSource = PropertyDetailMapRouteOriginSource.MY_LOCATION
        myLocationRouteOrigin = null
        customRouteOrigin = null
    }

    LaunchedEffect(isRouteActive, userLocationState) {
        if (!isRouteActive) return@LaunchedEffect
        if (routeOriginSource != PropertyDetailMapRouteOriginSource.MY_LOCATION) return@LaunchedEffect
        if (myLocationRouteOrigin != null) return@LaunchedEffect
        myLocationRouteOrigin = userLocationState
    }

    LaunchedEffect(
        isRouteActive,
        selectedRouteMode,
        routeOrigin?.lat,
        routeOrigin?.lon,
        mapRefs?.first,
    ) {
        if (!isRouteActive) return@LaunchedEffect
        val mapView = mapRefs?.first ?: return@LaunchedEffect
        val origin = routeOrigin ?: run {
            routeSummary = null
            isRouteLoading = false
            return@LaunchedEffect
        }
        requestPropertyDetailMapRoute(
            mapView = mapView,
            routeController = routeController,
            property = property,
            selectedMode = selectedRouteMode,
            isDarkTheme = isDarkTheme,
            origin = origin,
            routeErrorMessage = routeErrorMessage,
            onLoading = { isRouteLoading = true },
            onSuccess = { summary ->
                isRouteLoading = false
                routeSummary = summary
            },
            onFailure = {
                isRouteLoading = false
                routeSummary = null
            },
        )
    }

    LaunchedEffect(
        isRouteActive,
        routeOriginSource,
        customRouteOrigin?.lat,
        customRouteOrigin?.lon,
        mapRefs?.first,
        mapRefs?.second,
    ) {
        val mapView = mapRefs?.first ?: return@LaunchedEffect
        val mapHelper = mapRefs?.second ?: return@LaunchedEffect
        if (!isRouteActive) {
            routeOriginOverlay.clear(mapView)
            return@LaunchedEffect
        }
        if (routeOriginSource == PropertyDetailMapRouteOriginSource.CUSTOM) {
            routeOriginOverlay.update(
                mapView = mapView,
                mapHelper = mapHelper,
                context = context,
                origin = customRouteOrigin,
            )
        } else {
            routeOriginOverlay.clear(mapView)
        }
    }

    PropertyDetailMapRouteOriginMapPickerEffect(
        mapView = mapRefs?.first,
        isActive = isRouteActive && isPickingRouteOrigin,
        onPointPicked = { lat, lon ->
            onCustomRouteOriginPicked(lat, lon)
            mapRefs?.first?.let { mapView ->
                mapRefs?.second?.let { mapHelper ->
                    routeOriginOverlay.update(
                        mapView = mapView,
                        mapHelper = mapHelper,
                        context = context,
                        origin = UserLocation(lat = lat, lon = lon, bearing = 0f),
                    )
                }
            }
        },
    )
    LaunchedEffect(isDarkTheme, isRouteActive, mapRefs?.first, routeSummary) {
        if (!isRouteActive || routeSummary == null) return@LaunchedEffect
        mapRefs?.first?.let { mapView ->
            routeController.updateTheme(mapView, isDarkTheme)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapRefs?.first?.let { mapView ->
                routeController.release(mapView)
            }
        }
    }

    val visiblePois = remember(nearbyMapPois, visiblePoiCategories) {
        nearbyMapPois.filter { it.category in visiblePoiCategories }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
            ) {
                PropertyDetailMapHost(
                    property = property,
                    visibleMapPois = visiblePois,
                    modifier = Modifier.fillMaxSize(),
                    trackUserLocation = true,
                    locationPermissionGranted = locationPermissionGranted,
                    onMapReady = { mv, mh -> mapRefs = mv to mh },
                    onUserLocationUpdate = { userLocationState = it },
                )

                PropertyDetailFullscreenMapZoomControls(
                    mapView = mapRefs?.first,
                    mapHelper = mapRefs?.second,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )

                PropertyDetailFullscreenMapMyLocationFab(
                    mapView = mapRefs?.first,
                    mapHelper = mapRefs?.second,
                    userLocation = userLocationState,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .navigationBarsPadding()
                        .padding(
                            start = PaddingMedium,
                            bottom = routeMapControlsBottomInset(
                                isRouteActive = isRouteActive,
                                isRouteSheetVisible = isRouteSheetVisible,
                            ),
                        ),
                )

                PropertyDetailFullscreenMapRouteToggle(
                    expanded = isRouteActive,
                    onToggle = {
                        if (isRouteActive) {
                            closeRoutePanel()
                        } else {
                            openRoutePanel()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .navigationBarsPadding()
                        .padding(
                            end = PaddingMedium,
                            bottom = routeMapControlsBottomInset(
                                isRouteActive = isRouteActive,
                                isRouteSheetVisible = isRouteSheetVisible,
                            ),
                        ),
                )

                PropertyDetailFullscreenMapPoiLayersToggle(
                    active = isPoiLayersActive,
                    onToggle = {
                        if (isPoiLayersActive) {
                            closePoiLayersPanel()
                        } else {
                            openPoiLayersPanel()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(PaddingMedium),
                )

                PropertyDetailFullscreenMapPoiLayersPanel(
                    sheetVisible = isPoiLayersActive && isPoiLayersSheetVisible,
                    visibleCategories = visiblePoiCategories,
                    onSetCategoryVisible = onSetPoiCategoryVisible,
                    nearbyMapPois = nearbyMapPois,
                    visiblePois = visiblePois,
                    isNearbyPoisLoading = isNearbyPoisLoading,
                    nearbyPoisLoadFailed = nearbyPoisLoadFailed,
                    onMinimizeSheet = { minimizePoiLayersSheet() },
                    onDismiss = { closePoiLayersPanel() },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(
                            start = PaddingMedium,
                            end = PaddingMedium,
                            top = 56.dp,
                        ),
                )

                if (isPoiLayersActive && !isPoiLayersSheetVisible) {
                    PropertyDetailFullscreenMapPoiLayersRestoreBar(
                        nearbyMapPois = nearbyMapPois,
                        visiblePois = visiblePois,
                        isNearbyPoisLoading = isNearbyPoisLoading,
                        nearbyPoisLoadFailed = nearbyPoisLoadFailed,
                        onRestore = { restorePoiLayersSheet() },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .padding(
                                start = PaddingMedium,
                                end = PaddingMedium,
                                top = 56.dp,
                            ),
                    )
                }

                PropertyDetailFullscreenMapRoutePanel(
                    sheetVisible = isRouteActive && isRouteSheetVisible,
                    destinationTitle = property.title,
                    routeOriginSource = routeOriginSource,
                    isPickingRouteOrigin = isPickingRouteOrigin,
                    onUseMyLocation = { useMyLocationForRoute() },
                    onPickRouteOriginOnMap = { startPickRouteOriginOnMap() },
                    selectedMode = selectedRouteMode,
                    onModeSelected = {
                        selectedRouteMode = it
                        routeSummary = null
                        isRouteLoading = true
                    },
                    isRouteLoading = isRouteLoading,
                    routeSummary = routeSummary,
                    hasRouteOrigin = hasRouteOrigin,
                    onMinimizeSheet = { minimizeRouteSheet() },
                    onDismiss = { closeRoutePanel() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )

                if (isRouteActive && isPickingRouteOrigin) {
                    PropertyDetailMapRoutePickOriginBanner(
                        onCancel = { cancelPickRouteOrigin() },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .padding(
                                start = PaddingMedium,
                                end = PaddingMedium,
                                top = 56.dp,
                            ),
                    )
                }

                if (isRouteActive && !isRouteSheetVisible) {
                    PropertyDetailFullscreenMapRouteRestoreBar(
                        routeSummary = routeSummary,
                        isRouteLoading = isRouteLoading,
                        onRestore = { restoreRouteSheet() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(
                                horizontal = PaddingMedium,
                                vertical = PaddingMedium,
                            ),
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(PaddingMedium)
                        .background(
                            MapFullscreenControlScrim,
                            RoundedCornerShape(PropertyDetailMapCornerRadius),
                        ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = MapControlsIconOnDark,
                    )
                }

            }
        }
    }
}

private fun routeMapControlsBottomInset(
    isRouteActive: Boolean,
    isRouteSheetVisible: Boolean,
): Dp = when {
    isRouteActive && isRouteSheetVisible -> MapRouteFullscreenBottomSheetInset
    isRouteActive -> 72.dp
    else -> PaddingMedium
}
