package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.PropertyDetailInfoLabelFontSize
import com.example.propertymanagement.ui.theme.PropertyDetailInfoLabelLineHeight
import com.example.propertymanagement.ui.theme.PropertyDetailInfoValueFontSize
import com.example.propertymanagement.ui.theme.PropertyDetailInfoValueLineHeight

@Composable
fun PropertyDetailInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val labelStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = PropertyDetailInfoLabelFontSize,
        lineHeight = PropertyDetailInfoLabelLineHeight
    )
    val valueStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = PropertyDetailInfoValueFontSize,
        lineHeight = PropertyDetailInfoValueLineHeight,
        fontWeight = FontWeight.Medium
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = PaddingSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = labelStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                style = valueStyle,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }

    }
}
