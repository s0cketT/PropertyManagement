package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
internal fun RouteModeOverviewSection(
    summary: PropertyDetailMapRouteSummary,
    mode: PropertyDetailMapRouteMode,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
    ) {
        RouteMetricChip(
            icon = mode.icon,
            primaryText = formatRouteDurationPrimary(summary),
            secondaryText = formatRouteDurationSecondary(summary),
            caption = stringResource(R.string.property_detail_map_route_duration_in_transit),
            emphasized = true,
            modifier = Modifier.weight(1f),
        )
        RouteMetricChip(
            icon = Icons.Default.NearMe,
            primaryText = summary.distanceText,
            secondaryText = null,
            caption = stringResource(R.string.property_detail_map_route_distance_label),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
internal fun RouteTransitTransfersOverviewSection(
    transfersCount: Int,
    modifier: Modifier = Modifier,
) {
    if (transfersCount <= 0) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
    ) {
        RouteMetricChip(
            icon = Icons.Default.SwapHoriz,
            primaryText = transfersCount.toString(),
            secondaryText = null,
            caption = pluralStringResource(
                R.plurals.property_detail_map_route_transfers,
                transfersCount,
                transfersCount,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun RouteMetricChip(
    icon: ImageVector,
    primaryText: String,
    secondaryText: String?,
    caption: String,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (emphasized) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (emphasized) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(18.dp),
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = primaryText,
                    style = if (emphasized) {
                        MaterialTheme.typography.headlineSmall
                    } else {
                        MaterialTheme.typography.titleMedium
                    },
                    fontWeight = FontWeight.Bold,
                    color = if (emphasized) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                secondaryText?.let { unit ->
                    Text(
                        text = " $unit",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (emphasized) {
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                }
            }
            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall,
                color = if (emphasized) {
                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun formatRouteDurationPrimary(summary: PropertyDetailMapRouteSummary): String {
    if (summary.durationSeconds <= 0.0) return summary.durationText
    return formatRouteDuration(summary.durationSeconds).value
}

private fun formatRouteDurationSecondary(summary: PropertyDetailMapRouteSummary): String? {
    if (summary.durationSeconds <= 0.0) return null
    return formatRouteDuration(summary.durationSeconds).unit
}
