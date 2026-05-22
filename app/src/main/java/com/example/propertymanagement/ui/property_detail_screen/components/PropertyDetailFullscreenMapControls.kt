package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.ui.components.MapHelper
import com.example.propertymanagement.ui.theme.MapSizesColors
import com.example.propertymanagement.ui.theme.PaddingExtraLarge
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerHeightTight
import com.yandex.mapkit.mapview.MapView

@Composable
internal fun PropertyDetailFullscreenMapZoomControls(
    mapView: MapView?,
    mapHelper: MapHelper?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(PaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SmallFloatingActionButton(
            onClick = {
                val mv = mapView ?: return@SmallFloatingActionButton
                val mh = mapHelper ?: return@SmallFloatingActionButton
                mh.zoomByDelta(
                    mapView = mv,
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
                val mv = mapView ?: return@SmallFloatingActionButton
                val mh = mapHelper ?: return@SmallFloatingActionButton
                mh.zoomByDelta(
                    mapView = mv,
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
}

@Composable
internal fun PropertyDetailFullscreenMapMyLocationFab(
    mapView: MapView?,
    mapHelper: MapHelper?,
    userLocation: UserLocation?,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = {
            val loc = userLocation ?: return@FloatingActionButton
            val mv = mapView ?: return@FloatingActionButton
            val mh = mapHelper ?: return@FloatingActionButton
            mh.moveCameraTo(
                mapView = mv,
                latitude = loc.lat,
                longitude = loc.lon,
            )
        },
        modifier = modifier
            .navigationBarsPadding()
            .padding(PaddingExtraLarge),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Icon(
            imageVector = Icons.Default.MyLocation,
            contentDescription = stringResource(R.string.map_my_location_content_description),
        )
    }
}

