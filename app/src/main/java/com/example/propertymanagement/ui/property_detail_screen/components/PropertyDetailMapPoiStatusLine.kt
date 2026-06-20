package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
internal fun resolvePoiStatusContent(
    nearbyMapPois: List<NearbyMapPoi>,
    visiblePois: List<NearbyMapPoi>,
    isNearbyPoisLoading: Boolean,
    nearbyPoisLoadFailed: Boolean,
): PoiStatusContent? {
    return when {
        isNearbyPoisLoading -> PoiStatusContent(
            message = stringResource(R.string.property_detail_nearby_pois_loading),
            showProgress = true,
        )

        nearbyPoisLoadFailed -> PoiStatusContent(
            message = stringResource(R.string.property_detail_nearby_pois_error),
            isError = true,
        )

        nearbyMapPois.isEmpty() -> PoiStatusContent(
            message = stringResource(R.string.property_detail_nearby_pois_empty_area),
        )

        visiblePois.isNotEmpty() -> PoiStatusContent(
            message = stringResource(
                R.string.property_detail_nearby_pois_shown,
                visiblePois.size,
                nearbyMapPois.size,
            ),
            isSuccess = true,
        )

        else -> null
    }
}

@Composable
internal fun MapPoiLayerStatusRow(
    content: PoiStatusContent,
    style: MapPoiLayerPanelStyle,
    modifier: Modifier = Modifier,
) {
    val textColor = when {
        content.isError -> MaterialTheme.colorScheme.error
        style == MapPoiLayerPanelStyle.OnMapOverlay -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val iconTint = when {
        content.isError -> MaterialTheme.colorScheme.error
        content.isSuccess -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
    ) {
        when {
            content.showProgress -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(15.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            content.isError -> {
                StatusIcon(
                    imageVector = Icons.Outlined.WarningAmber,
                    tint = iconTint,
                )
            }
            content.isSuccess -> {
                StatusIcon(
                    imageVector = Icons.Outlined.LocationOn,
                    tint = iconTint,
                )
            }
            else -> {
                StatusIcon(
                    imageVector = Icons.Outlined.Info,
                    tint = iconTint,
                )
            }
        }

        Text(
            text = content.message,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
        )
    }
}

@Composable
private fun StatusIcon(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier.size(16.dp),
        tint = tint,
    )
}

internal data class PoiStatusContent(
    val message: String,
    val showProgress: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
)
