package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.LocalAppDarkTheme
import com.example.propertymanagement.ui.theme.MapFullscreenControlScrim
import com.example.propertymanagement.ui.theme.MapPoiLayerColors
import com.example.propertymanagement.ui.theme.MapPoiLayerTileAccentHeight
import com.example.propertymanagement.ui.theme.MapPoiLayerTileIconCircleSize
import com.example.propertymanagement.ui.theme.MapPoiLayerTileIconSize
import com.example.propertymanagement.ui.theme.MapPoiLayersPanelCornerRadius
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.SurfaceTonalElevationLow

enum class MapPoiLayerPanelStyle {
    Default,
    OnMapOverlay,
}

@Composable
internal fun PropertyDetailFullscreenMapPoiLayersToggle(
    active: Boolean,
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
            imageVector = if (active) Icons.Default.Close else Icons.Default.Layers,
            contentDescription = stringResource(
                if (active) {
                    R.string.property_detail_map_close_poi_layers_cd
                } else {
                    R.string.property_detail_map_open_poi_layers_cd
                },
            ),
            tint = Color.White,
        )
    }
}

@Composable
internal fun PropertyDetailFullscreenMapPoiLayersPanel(
    sheetVisible: Boolean,
    visibleCategories: Set<NearbyPoiCategory>,
    onSetCategoryVisible: (NearbyPoiCategory, Boolean) -> Unit,
    nearbyMapPois: List<NearbyMapPoi>,
    visiblePois: List<NearbyMapPoi>,
    isNearbyPoisLoading: Boolean,
    nearbyPoisLoadFailed: Boolean,
    onMinimizeSheet: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = sheetVisible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier,
    ) {
        val isDarkTheme = LocalAppDarkTheme.current
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(MapPoiLayersPanelCornerRadius),
            color = MaterialTheme.colorScheme.surface.copy(
                alpha = if (isDarkTheme) 0.92f else 0.94f,
            ),
            tonalElevation = SurfaceTonalElevationLow,
            shadowElevation = 6.dp,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                PoiLayersSheetDragHandle(onMinimize = onMinimizeSheet)
                PoiLayersSheetHeader(
                    onMinimize = onMinimizeSheet,
                    onDismiss = onDismiss,
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                    thickness = DividerThickness,
                )
                PropertyDetailMapPoiLayersBody(
                    visibleCategories = visibleCategories,
                    onSetCategoryVisible = onSetCategoryVisible,
                    nearbyMapPois = nearbyMapPois,
                    visiblePois = visiblePois,
                    isNearbyPoisLoading = isNearbyPoisLoading,
                    nearbyPoisLoadFailed = nearbyPoisLoadFailed,
                    style = MapPoiLayerPanelStyle.OnMapOverlay,
                )
            }
        }
    }
}

@Composable
internal fun PropertyDetailFullscreenMapPoiLayersRestoreBar(
    nearbyMapPois: List<NearbyMapPoi>,
    visiblePois: List<NearbyMapPoi>,
    isNearbyPoisLoading: Boolean,
    nearbyPoisLoadFailed: Boolean,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = LocalAppDarkTheme.current
    val statusContent = resolvePoiStatusContent(
        nearbyMapPois = nearbyMapPois,
        visiblePois = visiblePois,
        isNearbyPoisLoading = isNearbyPoisLoading,
        nearbyPoisLoadFailed = nearbyPoisLoadFailed,
    )

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
                    imageVector = Icons.Default.Layers,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Column {
                    Text(
                        text = stringResource(R.string.property_detail_poi_layers_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    statusContent?.let { content ->
                        Text(
                            text = content.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.property_detail_map_restore_poi_layers_cd),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PoiLayersSheetDragHandle(onMinimize: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onMinimize)
            .padding(top = 8.dp, bottom = 2.dp),
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
private fun PoiLayersSheetHeader(
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
            text = stringResource(R.string.property_detail_poi_layers_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row {
            IconButton(onClick = onMinimize) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.property_detail_map_minimize_poi_layers_cd),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.property_detail_map_close_poi_layers_cd),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun PropertyDetailMapPoiLayersPanel(
    visibleCategories: Set<NearbyPoiCategory>,
    onSetCategoryVisible: (NearbyPoiCategory, Boolean) -> Unit,
    nearbyMapPois: List<NearbyMapPoi>,
    visiblePois: List<NearbyMapPoi>,
    isNearbyPoisLoading: Boolean,
    nearbyPoisLoadFailed: Boolean,
    modifier: Modifier = Modifier,
    style: MapPoiLayerPanelStyle = MapPoiLayerPanelStyle.Default,
) {
    val isDarkTheme = LocalAppDarkTheme.current
    val panelColor = when (style) {
        MapPoiLayerPanelStyle.Default -> MaterialTheme.colorScheme.surface
        MapPoiLayerPanelStyle.OnMapOverlay -> MaterialTheme.colorScheme.surface.copy(
            alpha = if (isDarkTheme) 0.92f else 0.94f,
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(MapPoiLayersPanelCornerRadius),
        color = panelColor,
        tonalElevation = SurfaceTonalElevationLow,
        shadowElevation = if (style == MapPoiLayerPanelStyle.OnMapOverlay) 6.dp else 1.dp,
    ) {
        PropertyDetailMapPoiLayersBody(
            visibleCategories = visibleCategories,
            onSetCategoryVisible = onSetCategoryVisible,
            nearbyMapPois = nearbyMapPois,
            visiblePois = visiblePois,
            isNearbyPoisLoading = isNearbyPoisLoading,
            nearbyPoisLoadFailed = nearbyPoisLoadFailed,
            style = style,
        )
    }
}

@Composable
private fun PropertyDetailMapPoiLayersBody(
    visibleCategories: Set<NearbyPoiCategory>,
    onSetCategoryVisible: (NearbyPoiCategory, Boolean) -> Unit,
    nearbyMapPois: List<NearbyMapPoi>,
    visiblePois: List<NearbyMapPoi>,
    isNearbyPoisLoading: Boolean,
    nearbyPoisLoadFailed: Boolean,
    style: MapPoiLayerPanelStyle,
) {
    val dividerColor = when (style) {
        MapPoiLayerPanelStyle.Default -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
        MapPoiLayerPanelStyle.OnMapOverlay -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = PaddingSmall,
                    vertical = PaddingMedium,
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            NearbyPoiCategory.entries.forEach { category ->
                MapPoiLayerTile(
                    category = category,
                    selected = category in visibleCategories,
                    onToggle = { onSetCategoryVisible(category, it) },
                    style = style,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        val statusContent = resolvePoiStatusContent(
            nearbyMapPois = nearbyMapPois,
            visiblePois = visiblePois,
            isNearbyPoisLoading = isNearbyPoisLoading,
            nearbyPoisLoadFailed = nearbyPoisLoadFailed,
        )
        if (statusContent != null) {
            HorizontalDivider(
                color = dividerColor,
                thickness = DividerThickness,
            )
            MapPoiLayerStatusRow(
                content = statusContent,
                style = style,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = PaddingMedium,
                        vertical = PaddingSmall + 2.dp,
                    ),
            )
        }
    }
}

@Composable
private fun MapPoiLayerTile(
    category: NearbyPoiCategory,
    selected: Boolean,
    onToggle: (Boolean) -> Unit,
    style: MapPoiLayerPanelStyle,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = LocalAppDarkTheme.current
    val accent = MapPoiLayerColors.composeColor(category)
    val circleColor by animateColorAsState(
        targetValue = when {
            selected -> accent.copy(alpha = if (style == MapPoiLayerPanelStyle.OnMapOverlay) 1f else 0.16f)
            else -> when (style) {
                MapPoiLayerPanelStyle.Default -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                MapPoiLayerPanelStyle.OnMapOverlay -> if (isDarkTheme) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
                } else {
                    Color(0xFFF3F4F6)
                }
            }
        },
        label = "poiTileCircle",
    )
    val iconTint by animateColorAsState(
        targetValue = when {
            selected && style == MapPoiLayerPanelStyle.OnMapOverlay -> Color.White
            selected -> accent
            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f)
        },
        label = "poiTileIcon",
    )
    val labelColor by animateColorAsState(
        targetValue = when {
            selected -> accent
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "poiTileLabel",
    )
    val accentHeight by animateDpAsState(
        targetValue = if (selected) MapPoiLayerTileAccentHeight else 0.dp,
        label = "poiTileAccent",
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = { onToggle(!selected) },
            )
            .padding(vertical = PaddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MapPoiLayerTileIconCircleSize)
                .clip(CircleShape)
                .background(circleColor),
        ) {
            Icon(
                painter = painterResource(category.iconRes()),
                contentDescription = stringResource(category.labelRes()),
                tint = iconTint,
                modifier = Modifier.size(MapPoiLayerTileIconSize),
            )
        }

        Text(
            text = stringResource(category.labelRes()),
            style = MaterialTheme.typography.labelMedium,
            color = labelColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = SpacerSmall),
        )

        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .width(28.dp)
                .height(MapPoiLayerTileAccentHeight)
                .clip(RoundedCornerShape(50)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(accentHeight)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(50))
                    .background(accent),
            )
        }
    }
}

private fun NearbyPoiCategory.labelRes(): Int =
    when (this) {
        NearbyPoiCategory.SCHOOL -> R.string.property_detail_poi_category_school
        NearbyPoiCategory.POLYCLINIC -> R.string.property_detail_poi_category_clinic
        NearbyPoiCategory.GROCERY -> R.string.property_detail_poi_category_grocery
    }

private fun NearbyPoiCategory.iconRes(): Int =
    when (this) {
        NearbyPoiCategory.SCHOOL -> R.drawable.school_icon
        NearbyPoiCategory.POLYCLINIC -> R.drawable.clinick_icon
        NearbyPoiCategory.GROCERY -> R.drawable.shop_icon
    }
