package com.example.propertymanagement.ui.property_detail_screen.components

import android.Manifest
import android.content.pm.PackageManager
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
import com.example.propertymanagement.ui.theme.MapPreviewGradientEnd
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.example.propertymanagement.ui.theme.PropertyDetailMapPreviewHeight
import com.example.propertymanagement.ui.theme.SpacerTiny
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

        PropertyDetailMapPoiStatusLine(
            nearbyMapPois = nearbyMapPois,
            visiblePois = visiblePois,
            isNearbyPoisLoading = isNearbyPoisLoading,
            nearbyPoisLoadFailed = nearbyPoisLoadFailed,
            modifier = Modifier.padding(top = SpacerTiny),
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
                    modifier = Modifier.align(Alignment.BottomStart),
                )

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

                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = PaddingMedium, end = PaddingMedium)
                        .background(
                            MapFullscreenControlScrim,
                            RoundedCornerShape(PropertyDetailMapCornerRadius),
                        )
                        .padding(
                            start = PaddingMedium,
                            top = 6.dp,
                            end = PaddingMedium,
                            bottom = 6.dp,
                        ),
                    horizontalAlignment = Alignment.End,
                ) {
                    PropertyDetailMapPoiCategoryCheckboxes(
                        visibleCategories = visiblePoiCategories,
                        onSetCategoryVisible = onSetPoiCategoryVisible,
                        showSectionTitle = false,
                        forMapOverlay = true,
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                ) {
                    PropertyDetailMapPoiStatusLine(
                        nearbyMapPois = nearbyMapPois,
                        visiblePois = visiblePois,
                        isNearbyPoisLoading = isNearbyPoisLoading,
                        nearbyPoisLoadFailed = nearbyPoisLoadFailed,
                    )
                }
            }
        }
    }
}
