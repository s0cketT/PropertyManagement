package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.propertymanagement.ui.theme.CornerRadiusExtraSmall
import com.example.propertymanagement.ui.theme.SmallHorizontalPadding
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.SpacerTiny
import com.example.propertymanagement.ui.theme.VerticalPaddingCategory

/**
 * Заголовок секции без «карточного» вида: цветная полоска + текст.
 */
@Composable
fun PropertyDetailSectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = SpacerMedium, bottom = SpacerTiny),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(SmallHorizontalPadding)
                .height(VerticalPaddingCategory)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(CornerRadiusExtraSmall)
                )
        )
        Spacer(modifier = Modifier.width(SpacerSmall))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PropertyDetailSectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = SpacerMedium),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}
