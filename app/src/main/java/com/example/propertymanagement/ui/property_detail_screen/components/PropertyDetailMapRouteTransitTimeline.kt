package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall

private val TransitTimelineMaxHeight = 360.dp
private val TransitTimelineRailWidth = 28.dp
private val TransitTimelineNodeSize = 22.dp

@Composable
private fun RouteStepDurationChip(
    durationText: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
    ) {
        Text(
            text = durationText,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

@Composable
internal fun RouteTransitTimelineSection(
    steps: List<PropertyDetailMapRouteStep>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = PaddingSmall),
    ) {
        Text(
            text = stringResource(R.string.property_detail_map_transit_timeline_title),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                horizontal = PaddingMedium,
                vertical = PaddingSmall,
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = TransitTimelineMaxHeight)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = PaddingMedium,
                    end = PaddingMedium,
                    bottom = PaddingSmall,
                ),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            steps.forEachIndexed { index, step ->
                RouteTransitTimelineRow(
                    step = step,
                    isLast = index == steps.lastIndex,
                )
            }
        }
    }
}

@Composable
private fun RouteTransitTimelineRow(
    step: PropertyDetailMapRouteStep,
    isLast: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
    ) {
        TransitTimelineRail(
            step = step,
            drawTail = !isLast,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else PaddingMedium),
        ) {
            when (step.kind) {
                PropertyDetailMapRouteStepKind.RIDE -> TransitRideTimelineCard(step)
                PropertyDetailMapRouteStepKind.WALK -> TransitWalkTimelineCard(step)
                PropertyDetailMapRouteStepKind.TRANSFER -> TransitTransferTimelineCard(step)
                PropertyDetailMapRouteStepKind.WAIT -> Unit
            }
        }
    }
}

@Composable
private fun TransitTimelineRail(
    step: PropertyDetailMapRouteStep,
    drawTail: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(TransitTimelineRailWidth),
    ) {
        TransitTimelineNode(step = step)
        if (drawTail) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(if (step.kind == PropertyDetailMapRouteStepKind.RIDE) 4.dp else 2.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(timelineTailColor(step)),
            )
        }
    }
}

@Composable
private fun TransitTimelineNode(step: PropertyDetailMapRouteStep) {
    val nodeColor = when (step.kind) {
        PropertyDetailMapRouteStepKind.RIDE -> step.lineColorArgb?.let { Color(it) }
            ?: MaterialTheme.colorScheme.primary
        PropertyDetailMapRouteStepKind.WALK -> MaterialTheme.colorScheme.outline
        PropertyDetailMapRouteStepKind.TRANSFER -> MaterialTheme.colorScheme.tertiary
        PropertyDetailMapRouteStepKind.WAIT -> MaterialTheme.colorScheme.outline
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(TransitTimelineNodeSize)
            .clip(CircleShape)
            .background(nodeColor.copy(alpha = if (step.kind == PropertyDetailMapRouteStepKind.RIDE) 1f else 0.25f)),
    ) {
        Icon(
            imageVector = stepIcon(step.kind),
            contentDescription = null,
            tint = if (step.kind == PropertyDetailMapRouteStepKind.RIDE) Color.White else nodeColor,
            modifier = Modifier.size(14.dp),
        )
    }
}

@Composable
private fun TransitRideTimelineCard(step: PropertyDetailMapRouteStep) {
    val lineColor = step.lineColorArgb?.let { Color(it) } ?: MaterialTheme.colorScheme.primary

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = lineColor,
                modifier = Modifier.weight(1f, fill = false),
            ) {
                Text(
                    text = step.title?.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.property_detail_map_route_step_ride),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 6.dp,
                    ),
                )
            }
            step.durationText?.let { duration ->
                RouteStepDurationChip(
                    durationText = duration,
                    modifier = Modifier.padding(start = PaddingSmall),
                )
            }
        }

        if (step.waitUntilRideText != null || step.transportArrivesAtText != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
            ) {
                step.waitUntilRideText?.let { waitText ->
                    TransitArrivalChip(
                        text = stringResource(
                            R.string.property_detail_map_route_transit_arrives_in,
                            waitText,
                        ),
                    )
                }
                step.transportArrivesAtText?.let { arrivalTime ->
                    TransitArrivalChip(
                        text = stringResource(
                            R.string.property_detail_map_route_transit_arrives_at,
                            arrivalTime,
                        ),
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            step.boardStopName?.let { stop ->
                TransitStopLine(
                    prefix = stringResource(R.string.property_detail_map_transit_board_at),
                    stopName = stop,
                )
            }
            step.exitStopName?.let { stop ->
                TransitStopLine(
                    prefix = stringResource(R.string.property_detail_map_transit_get_off_at),
                    stopName = stop,
                )
            }
        }
    }
}

@Composable
private fun TransitArrivalChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun TransitWalkTimelineCard(step: PropertyDetailMapRouteStep) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = walkStepTitle(step),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            step.durationText?.let { duration ->
                RouteStepDurationChip(durationText = duration)
            }
        }
    }
}

@Composable
private fun TransitTransferTimelineCard(step: PropertyDetailMapRouteStep) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = transferStepTitle(step),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            step.durationText?.let { duration ->
                RouteStepDurationChip(durationText = duration)
            }
        }
    }
}

@Composable
private fun walkStepTitle(step: PropertyDetailMapRouteStep): String {
    val stopName = step.targetStopName?.takeIf { it.isNotBlank() }
    return when (step.walkTarget) {
        PropertyDetailMapRouteWalkTarget.TO_STOP -> stopName?.let {
            stringResource(R.string.property_detail_map_transit_walk_to_stop, it)
        } ?: stringResource(R.string.property_detail_map_route_step_walk)
        PropertyDetailMapRouteWalkTarget.FROM_STOP -> stopName?.let {
            stringResource(R.string.property_detail_map_transit_walk_from_stop, it)
        } ?: stringResource(R.string.property_detail_map_route_step_walk)
        PropertyDetailMapRouteWalkTarget.TO_DESTINATION -> {
            stringResource(R.string.property_detail_map_transit_walk_to_destination)
        }
        null -> stringResource(R.string.property_detail_map_route_step_walk)
    }
}

@Composable
private fun transferStepTitle(step: PropertyDetailMapRouteStep): String {
    val stopName = step.targetStopName?.takeIf { it.isNotBlank() }
    return stopName?.let {
        stringResource(R.string.property_detail_map_transit_transfer_to_stop, it)
    } ?: stringResource(R.string.property_detail_map_route_step_transfer)
}

@Composable
private fun TransitStopLine(
    prefix: String,
    stopName: String,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = prefix,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stopName,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false),
        )
    }
}

@Composable
private fun timelineTailColor(step: PropertyDetailMapRouteStep): Color = when (step.kind) {
    PropertyDetailMapRouteStepKind.RIDE -> step.lineColorArgb?.let { Color(it).copy(alpha = 0.55f) }
        ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
    else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)
}

private fun stepIcon(kind: PropertyDetailMapRouteStepKind): ImageVector = when (kind) {
    PropertyDetailMapRouteStepKind.WALK -> Icons.AutoMirrored.Filled.DirectionsWalk
    PropertyDetailMapRouteStepKind.RIDE -> Icons.Default.DirectionsBus
    PropertyDetailMapRouteStepKind.TRANSFER -> Icons.Default.SwapHoriz
    PropertyDetailMapRouteStepKind.WAIT -> Icons.Default.SwapHoriz
}
