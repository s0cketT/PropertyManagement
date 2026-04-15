package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.ui.components.MapHelper
import com.example.propertymanagement.ui.map_screen.components.observeMapLifecycle
import com.example.propertymanagement.ui.theme.MapControlsIconOnDark
import com.example.propertymanagement.ui.theme.MapFullscreenControlScrim
import com.example.propertymanagement.ui.theme.MapPreviewGradientEnd
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.CardElevationLow
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.example.propertymanagement.ui.theme.PropertyDetailMapPreviewHeight
import androidx.compose.foundation.layout.size
import com.example.propertymanagement.ui.theme.IconMedium
import com.yandex.mapkit.mapview.MapView

@Composable
fun PropertyDetailMapSection(
    property: Property,
    onOpenFullscreen: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(PropertyDetailMapPreviewHeight)
            .clip(RoundedCornerShape(PropertyDetailMapCornerRadius)),
        shape = RoundedCornerShape(PropertyDetailMapCornerRadius),
        tonalElevation = CardElevationLow,
        shadowElevation = CardElevationLow
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            PropertyDetailMapHost(
                property = property,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MapPreviewGradientEnd
                            )
                        )
                    )
            )

            IconButton(
                onClick = onOpenFullscreen,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(PaddingMedium)
                    .background(MapFullscreenControlScrim, RoundedCornerShape(PropertyDetailMapCornerRadius))
            ) {
                Icon(
                    imageVector = Icons.Filled.Fullscreen,
                    contentDescription = stringResource(R.string.property_detail_map_fullscreen_cd),
                    tint = MapControlsIconOnDark,
                    modifier = Modifier.size(IconMedium)
                )
            }
        }
    }
}

@Composable
fun PropertyDetailFullscreenMapDialog(
    property: Property,
    onDismiss: () -> Unit
) {
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
                PropertyDetailMapHost(
                    property = property,
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
            }
        }
    }
}

@Composable
private fun PropertyDetailMapHost(
    property: Property,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }
    val mapHelper = remember { MapHelper() }

    DisposableEffect(lifecycleOwner) {
        val observer = observeMapLifecycle(lifecycleOwner, mapView)
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapHelper.release(mapView)
        }
    }

    LaunchedEffect(property.id, property.latitude, property.longitude) {
        mapHelper.showSinglePropertyMarker(mapView, property)
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
