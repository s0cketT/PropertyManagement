package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.propertymanagement.ui.theme.ChipCornerRadius
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.DpZero
import com.example.propertymanagement.ui.theme.HeightFilterChip

@Preview
@Composable
fun SellerFilterChip(
    text: String = "",
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,

            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,

            selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderWidth = if (selected) DpZero else DividerThickness,
            borderColor = MaterialTheme.colorScheme.outline,
            selected = selected,
            enabled = true
        ),
        shape = RoundedCornerShape(ChipCornerRadius),
        modifier = Modifier.height(HeightFilterChip)
    )
}