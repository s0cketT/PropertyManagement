package com.example.propertymanagement.ui.map_screen.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.ApplyMapNightModeEffect
import com.example.propertymanagement.ui.components.MapHelper
import com.example.propertymanagement.ui.map.MapViewModel
import com.example.propertymanagement.ui.map_screen.MapEvent
import com.example.propertymanagement.ui.map_screen.MapIntent
import com.example.propertymanagement.ui.map_screen.MapState
import com.example.propertymanagement.ui.theme.IconSizeActionSquare
import com.example.propertymanagement.ui.theme.MapSizesColors
import com.example.propertymanagement.ui.theme.PaddingExtraLarge
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerHeightTight
import com.example.propertymanagement.ui.theme.Spacing12
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map as YandexMap
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    navController: NavController,
) {

    val mapViewModel: MapViewModel = koinViewModel<MapViewModel>()
    val state by mapViewModel.state.collectAsStateWithLifecycle()
    val intent = mapViewModel::processIntent
    val event: Flow<MapEvent> by remember { mutableStateOf(mapViewModel.event) }

    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            intent(MapIntent.LoadUserLocation)
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                locationPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            intent(MapIntent.LoadUserLocation)
        } else {
            permissionLauncher.launch(locationPermission)
        }
    }

    LaunchedEffect(Unit) {
        event.filterIsInstance<MapEvent>().collect { event ->
            when (event) {
                is MapEvent.NavigateBack -> navController.popBackStack()
                is MapEvent.NavigateFilterScreen -> navController.navigate(Screens.Filters.route)
                is MapEvent.ShowAuthRequiredForFavoritesOnly -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.auth_required),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                is MapEvent.NavigateToPropertyDetail -> {
                    navController.navigate(
                        Screens.PropertyDetailScreen.createRoute(
                            propertyId = event.propertyId,
                            userId = event.userId
                        )
                    )
                }
            }
        }
    }

    BackHandler {
        if (state.selectedMarkerProperty != null) {
            intent(MapIntent.DismissMarkerBottomSheet)
        } else {
            intent(MapIntent.NavigateBack)
        }
    }

    UI(state = state, intent = intent)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UI(
    state: MapState,
    intent: (MapIntent) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember { MapView(context) }
    val mapHelper = remember { MapHelper() }

    // Lifecycle карты
    DisposableEffect(lifecycleOwner) {
        val observer = observeMapLifecycle(lifecycleOwner, mapView)
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    //Camera listener
    DisposableEffect(Unit) {
        onDispose {
            mapHelper.release(mapView)
        }
    }

    val hasRegionFilter = !state.filtersProperty?.selectedRegionName.isNullOrBlank()

    ApplyMapNightModeEffect(mapView = mapView)

    // Геолокация обновляется часто, поэтому держим её отдельно, чтобы не трогать слой объявлений.
    LaunchedEffect(state.userLocation, hasRegionFilter) {
        state.userLocation?.let { userLocation ->
            mapHelper.updateUserLocation(
                mapView = mapView,
                location = userLocation,
                moveCameraOnFirstFix = !hasRegionFilter,
            )
        }
    }

    val markerDisplayCurrency = state.filtersProperty?.selectedCurrency ?: CurrencyType.USD

    // Метки объявлений перерисовываются только когда меняется набор/курс/выбранная валюта отображения.
    LaunchedEffect(state.filteredMarkers, state.currencyRates, markerDisplayCurrency, state.managerCommissionPercent) {
        mapHelper.showPropertyMarkers(
            mapView = mapView,
            markers = state.filteredMarkers,
            currencyRates = state.currencyRates,
            displayCurrency = markerDisplayCurrency,
            managerCommissionPercent = state.managerCommissionPercent,
        ) { property ->
            intent(MapIntent.MarkerTapped(property))
        }
    }

    LaunchedEffect(
        hasRegionFilter,
        state.filtersProperty?.selectedLocationLat,
        state.filtersProperty?.selectedLocationLng,
        state.filteredMarkers,
    ) {
        val lat = state.filtersProperty?.selectedLocationLat
        val lng = state.filtersProperty?.selectedLocationLng
        if (hasRegionFilter) {
            if (lat != null && lng != null) {
                mapHelper.moveCameraTo(
                    mapView = mapView,
                    latitude = lat,
                    longitude = lng,
                )
            } else {
                state.filteredMarkers.firstOrNull()?.let { property ->
                    mapHelper.moveCameraTo(
                        mapView = mapView,
                        latitude = property.latitude,
                        longitude = property.longitude,
                    )
                }
            }
        }
    }

    var visiblePropertyCount by remember { mutableIntStateOf(0) }
    val filteredMarkers by rememberUpdatedState(state.filteredMarkers)

    LaunchedEffect(state.filteredMarkers, mapView) {
        visiblePropertyCount = mapHelper.countPropertiesInVisibleRegion(
            mapView = mapView,
            markers = state.filteredMarkers,
        )
    }

    val visibleCountListener = remember(mapView, mapHelper) {
        object : CameraListener {
            override fun onCameraPositionChanged(
                map: YandexMap,
                cameraPosition: CameraPosition,
                cameraUpdateReason: CameraUpdateReason,
                finished: Boolean,
            ) {
                visiblePropertyCount = mapHelper.countPropertiesInVisibleRegion(
                    mapView = mapView,
                    markers = filteredMarkers,
                )
            }
        }
    }

    DisposableEffect(mapView, visibleCountListener) {
        mapView.map.addCameraListener(visibleCountListener)
        onDispose {
            mapView.map.removeCameraListener(visibleCountListener)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // Кнопка "Назад"
        IconButton(
            onClick = {
                if (state.selectedMarkerProperty != null) {
                    intent(MapIntent.DismissMarkerBottomSheet)
                } else {
                    intent(MapIntent.NavigateBack)
                }
            },
            modifier = Modifier
                .padding(PaddingLarge)
                .size(IconSizeActionSquare)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .padding(PaddingLarge)
                .align(Alignment.TopEnd),
            horizontalAlignment = Alignment.End,
        ) {
            MapDealTypeLegend()
            Spacer(modifier = Modifier.height(Spacing12))
            FilterChip(
                modifier = Modifier.width(170.dp),
                selected = state.showFavoritesOnly,
                onClick = { intent(MapIntent.ToggleShowFavoritesOnly) },
                label = {
                    Text(
                        text = stringResource(R.string.map_favorites_only),
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = if (state.showFavoritesOnly) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Filled.FavoriteBorder
                        },
                        contentDescription = null,
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.Black.copy(alpha = 0.55f),
                    labelColor = Color.White,
                    iconColor = Color.White,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }

        Column(
            modifier = Modifier
                .padding(PaddingLarge)
                .align(Alignment.CenterEnd),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SmallFloatingActionButton(
                onClick = {
                    mapHelper.zoomByDelta(
                        mapView = mapView,
                        delta = MapSizesColors.MAP_ZOOM_BUTTON_STEP,
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.map_zoom_in_content_description),
                )
            }
            Spacer(modifier = Modifier.height(SpacerHeightTight))
            SmallFloatingActionButton(
                onClick = {
                    mapHelper.zoomByDelta(
                        mapView = mapView,
                        delta = -MapSizesColors.MAP_ZOOM_BUTTON_STEP,
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = stringResource(R.string.map_zoom_out_content_description),
                )
            }
        }

        FloatingActionButton(
            onClick = {
                val location = state.userLocation
                if (location != null) {
                    mapHelper.moveCameraTo(
                        mapView = mapView,
                        latitude = location.lat,
                        longitude = location.lon,
                    )
                } else {
                    intent(MapIntent.LoadUserLocation)
                }
            },
            modifier = Modifier
                .padding(PaddingExtraLarge)
                .align(Alignment.BottomStart),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = stringResource(R.string.map_my_location_content_description)
            )
        }

        FloatingActionButton(
            onClick = {
                intent(MapIntent.NavigateFilterScreen)
            },
            modifier = Modifier
                .padding(PaddingExtraLarge)
                .align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = stringResource(R.string.filters_fab_content_description)
            )
        }

        if (state.filteredMarkers.isNotEmpty()) {
            MapVisiblePropertiesCountBanner(
                visibleCount = visiblePropertyCount,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = PaddingExtraLarge),
            )
        }

        state.selectedMarkerProperty?.let { property ->
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { intent(MapIntent.DismissMarkerBottomSheet) },
                sheetState = sheetState
            ) {
                MapMarkerBottomSheetContent(
                    property = property,
                    currencyRates = state.currencyRates,
                    managerCommissionPercent = state.managerCommissionPercent,
                    onDetailsClick = { intent(MapIntent.NavigateToSelectedPropertyDetail) }
                )
            }
        }
    }
}
