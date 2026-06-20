package com.example.propertymanagement.ui.map_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
fun MapVisiblePropertiesCountBanner(
    visibleCount: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = pluralStringResource(
            R.plurals.map_visible_properties_count,
            visibleCount,
            visibleCount,
        ),
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(
                horizontal = PaddingMedium,
                vertical = PaddingSmall,
            ),
        style = MaterialTheme.typography.bodySmall,
        color = Color.White,
    )
}
