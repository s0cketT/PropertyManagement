package com.example.propertymanagement.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.HeightOutlinedTextField
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerSmall

@Composable
fun LabeledCheckboxSection(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = PaddingLarge),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeightOutlinedTextField),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SpacerSmall),
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = DividerThickness
        )
    }
}