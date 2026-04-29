package com.example.propertymanagement.ui.map_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
fun MapDealTypeLegend(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(170.dp)
            .background(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(
                horizontal = PaddingMedium,
                vertical = PaddingSmall,
            ),
    ) {
        LegendRow(
            label = stringResource(R.string.map_legend_sale),
            cheapColor = Color(0xFFDBEAFE),
            expensiveColor = Color(0xFF1D4ED8),
        )

        Spacer(modifier = Modifier.height(2.dp))

        LegendRow(
            label = stringResource(R.string.map_legend_rent),
            cheapColor = Color(0xFFFFEDD5),
            expensiveColor = Color(0xFFC2410C),
        )
    }
}

@Composable
private fun LegendRow(
    label: String,
    cheapColor: Color,
    expensiveColor: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
        )

        Row {
            ColorDot(color = cheapColor)
            Spacer(modifier = Modifier.width(6.dp))
            ColorDot(color = expensiveColor)
        }
    }
}

@Composable
private fun ColorDot(color: Color) {
    Spacer(
        modifier = Modifier
            .size(10.dp)
            .background(color = color, shape = CircleShape),
    )
}

