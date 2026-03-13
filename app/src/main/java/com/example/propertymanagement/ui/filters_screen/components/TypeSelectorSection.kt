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
import androidx.compose.material.icons.filled.Menu
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
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.theme.DividerColor
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.FilterChipSelected
import com.example.propertymanagement.ui.theme.HorizontalPadding
import com.example.propertymanagement.ui.theme.IconSizeArrow
import com.example.propertymanagement.ui.theme.IconSizeCategory
import com.example.propertymanagement.ui.theme.OnPrimary
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.UnselectedGray
import com.example.propertymanagement.ui.theme.VerticalPaddingCategory

@Composable
fun TypeSelectorSection(
    selectedType: PropertyType?,
    onClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val isSelected = selectedType != null

    val iconTint = if (isSelected) FilterChipSelected else UnselectedGray
    val textColor = if (isSelected) OnPrimary else UnselectedGray

    Column(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = HorizontalPadding,
                    vertical = VerticalPaddingCategory
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SpacerBetweenElements)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(IconSizeCategory)
            )

            Text(
                text = if (isSelected) {
                    stringResource(
                        when (selectedType) {
                            PropertyType.APARTMENT -> R.string.category_apartments
                            PropertyType.HOUSE -> R.string.category_houses
                            PropertyType.LAND -> R.string.category_land
                            PropertyType.COMMERCIAL -> R.string.category_commercial
                            PropertyType.GARAGE -> R.string.category_garages
                            PropertyType.ROOM -> R.string.category_rooms
                            null -> R.string.category
                        }
                    )
                } else {
                    stringResource(R.string.category)
                },
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                modifier = Modifier
                    .weight(1f)
            )


            if (isSelected) {
                IconButton(
                    onClick = onResetClick,
                    modifier = Modifier.size(IconSizeArrow)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Сбросить категорию",
                        tint = FilterChipSelected,
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = UnselectedGray,
                    modifier = Modifier.size(IconSizeArrow)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HorizontalPadding),
            thickness = DividerThickness,
            color = DividerColor
        )
    }
}