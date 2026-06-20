package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.propertymanagement.R

enum class PropertyDetailMapRouteMode(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    PEDESTRIAN(
        labelRes = R.string.property_detail_map_route_mode_pedestrian,
        icon = Icons.AutoMirrored.Filled.DirectionsWalk,
    ),
    DRIVING(
        labelRes = R.string.property_detail_map_route_mode_driving,
        icon = Icons.Filled.DirectionsCar,
    ),
    BICYCLE(
        labelRes = R.string.property_detail_map_route_mode_bicycle,
        icon = Icons.AutoMirrored.Filled.DirectionsBike,
    ),
    TRANSIT(
        labelRes = R.string.property_detail_map_route_mode_transit,
        icon = Icons.Filled.DirectionsBus,
    ),
}
