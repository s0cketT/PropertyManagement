package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.ui.theme.MapControlsIconOnDark
import com.example.propertymanagement.ui.theme.MapPoiCategoryCheckboxIconSize
import com.example.propertymanagement.ui.theme.MapPoiLayerColors
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.SpacerTiny

@Composable
fun PropertyDetailMapPoiCategoryCheckboxes(
    visibleCategories: Set<NearbyPoiCategory>,
    onSetCategoryVisible: (NearbyPoiCategory, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showSectionTitle: Boolean = true,
    /** Текст/контур поверх карты — светлые; контраст за счёт полупрозрачной подложки в разметке. */
    forMapOverlay: Boolean = false,
) {
    val columnTop = if (showSectionTitle) SpacerTiny else 0.dp
    val labelColor = if (forMapOverlay) {
        MapControlsIconOnDark
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val outlineColor = if (forMapOverlay) {
        MapControlsIconOnDark
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val checkmarkColor = if (forMapOverlay) MapControlsIconOnDark else MaterialTheme.colorScheme.surface

    Column(
        modifier = modifier.wrapContentWidth(align = Alignment.End),
    ) {
        if (showSectionTitle) {
            Text(
                text = stringResource(R.string.property_detail_poi_layers_title),
                style = MaterialTheme.typography.labelMedium,
                color = labelColor,
                modifier = Modifier.wrapContentWidth(align = Alignment.End),
                textAlign = TextAlign.End,
            )
        }

        Column(
            modifier = Modifier.padding(top = columnTop),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.End,
        ) {
            NearbyPoiCategory.entries.forEach { category ->
                val accent = MapPoiLayerColors.composeColor(category)
                val checked = category in visibleCategories
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Icon(
                        painter = painterResource(category.iconRes()),
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(MapPoiCategoryCheckboxIconSize),
                    )
                    Text(
                        text = stringResource(category.labelRes()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = labelColor,
                        modifier = Modifier.padding(start = PaddingSmall),
                    )
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { onSetCategoryVisible(category, it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = accent,
                            uncheckedColor = outlineColor,
                            checkmarkColor = checkmarkColor,
                        ),
                        modifier = Modifier.padding(start = PaddingMedium),
                    )
                }
            }
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
