package com.example.propertymanagement.ui.filters_screen.components

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
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.DividerColor
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.HeightOutlinedTextField
import com.example.propertymanagement.ui.theme.OnPrimary
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PrimaryBlue
import com.example.propertymanagement.ui.theme.SpacerSmall

@Composable
fun OnlyWithPhotosSection(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
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
                text = stringResource(R.string.filter_only_with_photos),
                style = MaterialTheme.typography.bodyLarge,
            )

            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = PrimaryBlue,
                    uncheckedColor = DividerColor,
                    checkmarkColor = OnPrimary
                ),
                modifier = Modifier.clickable { onCheckedChange(!isChecked) }
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SpacerSmall),
            color = DividerColor,
            thickness = DividerThickness
        )
    }
}