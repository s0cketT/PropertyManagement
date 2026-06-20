package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.LocalAppDarkTheme
import com.example.propertymanagement.ui.theme.MapFullscreenControlScrim
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.example.propertymanagement.ui.theme.SurfaceTonalElevationLow

private val RouteSheetTopCornerRadius = 24.dp

@Composable
internal fun PropertyDetailFullscreenMapRouteToggle(
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
            .background(
                MapFullscreenControlScrim,
                RoundedCornerShape(PropertyDetailMapCornerRadius),
            ),
    ) {
        Icon(
            imageVector = if (expanded) Icons.Default.Close else Icons.Filled.Directions,
            contentDescription = stringResource(
                if (expanded) {
                    R.string.property_detail_map_close_route_panel_cd
                } else {
                    R.string.property_detail_map_open_route_panel_cd
                },
            ),
            tint = Color.White,
        )
    }
}

@Composable
internal fun PropertyDetailFullscreenMapRoutePanel(
    sheetVisible: Boolean,
    destinationTitle: String,
    routeOriginSource: PropertyDetailMapRouteOriginSource,
    isPickingRouteOrigin: Boolean,
    onUseMyLocation: () -> Unit,
    onPickRouteOriginOnMap: () -> Unit,
    selectedMode: PropertyDetailMapRouteMode,
    onModeSelected: (PropertyDetailMapRouteMode) -> Unit,
    isRouteLoading: Boolean,
    routeSummary: PropertyDetailMapRouteSummary?,
    hasRouteOrigin: Boolean,
    onMinimizeSheet: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = sheetVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier,
    ) {
        val isDarkTheme = LocalAppDarkTheme.current
        val panelColor = MaterialTheme.colorScheme.surface.copy(
            alpha = if (isDarkTheme) 0.97f else 0.98f,
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(
                topStart = RouteSheetTopCornerRadius,
                topEnd = RouteSheetTopCornerRadius,
            ),
            color = panelColor,
            tonalElevation = SurfaceTonalElevationLow,
            shadowElevation = 12.dp,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                RouteSheetDragHandle(onMinimize = onMinimizeSheet)

                RouteSheetHeader(
                    onMinimize = onMinimizeSheet,
                    onDismiss = onDismiss,
                )

                RouteOriginEndpointsSection(
                    destinationTitle = destinationTitle,
                    originSource = routeOriginSource,
                    isPickingOrigin = isPickingRouteOrigin,
                    onUseMyLocation = onUseMyLocation,
                    onPickOnMap = onPickRouteOriginOnMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PaddingMedium),
                )

                Spacer(modifier = Modifier.height(PaddingSmall))

                RouteDetailsBlock(
                    hasRouteOrigin = hasRouteOrigin,
                    isRouteLoading = isRouteLoading,
                    selectedMode = selectedMode,
                    routeSummary = routeSummary,
                    modifier = Modifier.fillMaxWidth(),
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    thickness = DividerThickness,
                    modifier = Modifier.padding(
                        top = PaddingMedium,
                        bottom = PaddingSmall,
                    ),
                )

                RouteModeSegmentedRow(
                    selectedMode = selectedMode,
                    onModeSelected = onModeSelected,
                    enabled = !isRouteLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = PaddingMedium,
                            end = PaddingMedium,
                            bottom = PaddingMedium,
                        ),
                )
            }
        }
    }
}

@Composable
internal fun PropertyDetailFullscreenMapRouteRestoreBar(
    routeSummary: PropertyDetailMapRouteSummary?,
    isRouteLoading: Boolean,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = LocalAppDarkTheme.current
    Surface(
        onClick = onRestore,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface.copy(
            alpha = if (isDarkTheme) 0.94f else 0.96f,
        ),
        shadowElevation = 8.dp,
        tonalElevation = SurfaceTonalElevationLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingMedium, vertical = PaddingSmall + 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
                modifier = Modifier.weight(1f),
            ) {
                Icon(
                    imageVector = Icons.Filled.Directions,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                when {
                    isRouteLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = stringResource(R.string.property_detail_map_route_loading),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    routeSummary != null -> {
                        Text(
                            text = routeSummary.durationText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = routeSummary.distanceText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(R.string.property_detail_map_route_title),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.property_detail_map_restore_route_sheet_cd),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun RouteSheetDragHandle(onMinimize: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onMinimize)
            .padding(top = 10.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
        )
    }
}

@Composable
private fun RouteSheetHeader(
    onMinimize: () -> Unit,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = PaddingMedium, end = PaddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.property_detail_map_route_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row {
            IconButton(onClick = onMinimize) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.property_detail_map_minimize_route_sheet_cd),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.property_detail_map_close_route_panel_cd),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun RouteDetailsBlock(
    hasRouteOrigin: Boolean,
    isRouteLoading: Boolean,
    selectedMode: PropertyDetailMapRouteMode,
    routeSummary: PropertyDetailMapRouteSummary?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        when {
            !hasRouteOrigin -> {
                Text(
                    text = stringResource(R.string.property_detail_map_route_no_origin),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = PaddingMedium),
                )
            }
            isRouteLoading -> {
                Row(
                    modifier = Modifier.padding(horizontal = PaddingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(R.string.property_detail_map_route_loading),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            selectedMode == PropertyDetailMapRouteMode.TRANSIT && routeSummary != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PaddingMedium),
                    verticalArrangement = Arrangement.spacedBy(PaddingSmall),
                ) {
                    RouteTransitTransfersOverviewSection(
                        transfersCount = routeSummary.transfersCount,
                    )
                    RouteLiveDataBadge(summary = routeSummary)
                }
                if (routeSummary.transitSteps.isNotEmpty()) {
                    RouteTransitTimelineSection(steps = routeSummary.transitSteps)
                }
            }
            routeSummary != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PaddingMedium),
                    verticalArrangement = Arrangement.spacedBy(PaddingSmall),
                ) {
                    RouteModeOverviewSection(
                        summary = routeSummary,
                        mode = selectedMode,
                    )
                    RouteLiveDataBadge(summary = routeSummary)
                }
            }
            else -> {
                Text(
                    text = stringResource(R.string.property_detail_map_route_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = PaddingMedium),
                )
            }
        }
    }
}

@Composable
private fun RouteModeSegmentedRow(
    selectedMode: PropertyDetailMapRouteMode,
    onModeSelected: (PropertyDetailMapRouteMode) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            PropertyDetailMapRouteMode.entries.forEach { mode ->
                val selected = mode == selectedMode
                val accent = MaterialTheme.colorScheme.primary
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(enabled = enabled) { onModeSelected(mode) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selected) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        Color.Transparent
                    },
                    shadowElevation = if (selected) 1.dp else 0.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Icon(
                            imageVector = mode.icon,
                            contentDescription = stringResource(mode.labelRes),
                            tint = if (selected) accent else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp),
                        )
                        Text(
                            text = stringResource(mode.labelRes),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (selected) accent else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteLiveDataBadge(
    summary: PropertyDetailMapRouteSummary,
    modifier: Modifier = Modifier,
) {
    val labelRes = when {
        summary.usesLiveTraffic -> R.string.property_detail_map_route_live_traffic
        summary.usesLiveSchedule -> R.string.property_detail_map_route_live_schedule
        else -> null
    } ?: return

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}
