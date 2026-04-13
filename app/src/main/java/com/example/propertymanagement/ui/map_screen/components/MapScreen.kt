package com.example.propertymanagement.ui.map_screen.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.MapHelper
import com.example.propertymanagement.ui.map.MapViewModel
import com.example.propertymanagement.ui.map_screen.MapEvent
import com.example.propertymanagement.ui.map_screen.MapIntent
import com.example.propertymanagement.ui.map_screen.MapState
import com.example.propertymanagement.ui.theme.OnPrimary
import com.example.propertymanagement.ui.theme.PaddingExtraLarge
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
            }
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

    LaunchedEffect(state.filteredMarkers) {
        mapHelper.clearMarkers()

        if (state.filteredMarkers.isEmpty()) {
            mapHelper.clearMarkers()
            return@LaunchedEffect
        }

        mapHelper.showPropertyMarkers(mapView, state.filteredMarkers)
    }

    LaunchedEffect(state.userLocation) {
        val location = state.userLocation ?: return@LaunchedEffect
        mapHelper.updateUserLocation(mapView, location)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // Кнопка "Назад"
        IconButton(
            onClick = { intent(MapIntent.NavigateBack) },
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
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
            contentColor = OnPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = stringResource(R.string.filters_fab_content_description)
            )
        }
    }
}
