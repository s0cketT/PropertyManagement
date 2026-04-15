@file:OptIn(ExperimentalLayoutApi::class)

package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.propertymanagement.ui.theme.SpacerSmall

@Composable
fun PropertyDetailAmenityChips(labels: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(SpacerSmall),
        verticalArrangement = Arrangement.spacedBy(SpacerSmall)
    ) {
        labels.forEach { label ->
            AssistChip(
                onClick = {},
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    }
}
