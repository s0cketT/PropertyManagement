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
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.ui.theme.ChipCornerRadius
import com.example.propertymanagement.ui.theme.DividerColor
import com.example.propertymanagement.ui.theme.FilterChipIconSelected
import com.example.propertymanagement.ui.theme.FilterChipSelected
import com.example.propertymanagement.ui.theme.FilterChipTextSelected
import com.example.propertymanagement.ui.theme.FilterChipTextUnselected
import com.example.propertymanagement.ui.theme.HeightFilterChip

@Composable
fun SellerFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) FilterChipTextSelected else FilterChipTextUnselected
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            selectedContainerColor = FilterChipSelected,
            labelColor = FilterChipTextUnselected,
            selectedLabelColor = FilterChipTextSelected,
            selectedLeadingIconColor = FilterChipIconSelected
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderWidth = if (selected) 0.dp else 1.dp,
            borderColor = DividerColor,
            selected = selected,
            enabled = true
        ),
        shape = RoundedCornerShape(ChipCornerRadius),
        modifier = Modifier.height(HeightFilterChip)
    )
}