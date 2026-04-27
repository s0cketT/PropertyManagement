package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.HorizontalPadding
import com.example.propertymanagement.ui.theme.IconSizeArrow
import com.example.propertymanagement.ui.theme.IconSizeCategory
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.VerticalPaddingCategory

@Composable
fun RegionSelectorSection(
    selectedRegionName: String?,
    selectedCityCount: Int,
    onClick: () -> Unit,
    onResetClick: () -> Unit,
) {
    val hasSelection = !selectedRegionName.isNullOrBlank()

    val text = when {
        selectedRegionName.isNullOrBlank() -> stringResource(R.string.region)
        selectedCityCount > 0 -> {
            stringResource(
                R.string.region_with_cities_count,
                selectedRegionName,
                selectedCityCount,
            )
        }
        else -> selectedRegionName
    }

    val textColor = if (hasSelection) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = HorizontalPadding,
                    vertical = VerticalPaddingCategory,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SpacerBetweenElements),
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = if (hasSelection) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(IconSizeCategory),
            )

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                modifier = Modifier.weight(1f),
            )

            if (hasSelection) {
                IconButton(
                    onClick = onResetClick,
                    modifier = Modifier.size(IconSizeArrow),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(IconSizeArrow),
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HorizontalPadding),
            thickness = DividerThickness,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

