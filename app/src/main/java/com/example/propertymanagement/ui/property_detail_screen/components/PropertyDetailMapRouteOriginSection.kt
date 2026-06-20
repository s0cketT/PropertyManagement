package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditLocationAlt
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius

private val RouteEndpointDotSize = 10.dp
private val RouteEndpointLineWidth = 2.dp

@Composable
internal fun RouteOriginEndpointsSection(
    destinationTitle: String,
    originSource: PropertyDetailMapRouteOriginSource,
    isPickingOrigin: Boolean,
    onUseMyLocation: () -> Unit,
    onPickOnMap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PaddingSmall),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            RouteOriginEndpointRow(
                dotColor = MaterialTheme.colorScheme.primary,
                label = when (originSource) {
                    PropertyDetailMapRouteOriginSource.MY_LOCATION ->
                        stringResource(R.string.property_detail_map_route_endpoint_my_location)
                    PropertyDetailMapRouteOriginSource.CUSTOM ->
                        stringResource(R.string.property_detail_map_route_endpoint_custom)
                },
                highlighted = isPickingOrigin,
            )
            Box(
                modifier = Modifier
                    .padding(start = (RouteEndpointDotSize - RouteEndpointLineWidth) / 2)
                    .width(RouteEndpointLineWidth)
                    .height(14.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)),
            )
            RouteOriginEndpointRow(
                dotColor = MaterialTheme.colorScheme.error,
                label = destinationTitle,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
        ) {
            RouteOriginOptionChip(
                label = stringResource(R.string.property_detail_map_route_origin_use_my_location),
                icon = Icons.Default.MyLocation,
                selected = originSource == PropertyDetailMapRouteOriginSource.MY_LOCATION && !isPickingOrigin,
                onClick = onUseMyLocation,
                modifier = Modifier.weight(1f),
            )
            RouteOriginOptionChip(
                label = stringResource(R.string.property_detail_map_route_origin_pick_on_map),
                icon = Icons.Default.EditLocationAlt,
                selected = originSource == PropertyDetailMapRouteOriginSource.CUSTOM || isPickingOrigin,
                onClick = onPickOnMap,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
internal fun PropertyDetailMapRoutePickOriginBanner(
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(PropertyDetailMapCornerRadius),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingMedium, vertical = PaddingSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
                modifier = Modifier.weight(1f),
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = stringResource(R.string.property_detail_map_route_origin_pick_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.property_detail_map_route_origin_pick_cancel))
            }
        }
    }
}

@Composable
private fun RouteOriginEndpointRow(
    dotColor: Color,
    label: String,
    highlighted: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (highlighted) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                } else {
                    Color.Transparent
                },
            )
            .padding(vertical = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .size(RouteEndpointDotSize)
                .clip(CircleShape)
                .background(dotColor),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RouteOriginOptionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
