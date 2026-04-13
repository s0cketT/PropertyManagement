package com.example.propertymanagement.ui.settings_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.LanguageType
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.UnselectedGray

@Composable
fun LanguageSection(
    selectedLanguage: LanguageType,
    isVisible: Boolean,
    onClick: () -> Unit,
    onSelect: (LanguageType) -> Unit,
    onDismiss: () -> Unit
) {
    Column {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = PaddingLarge, vertical = PaddingMedium)
        ) {

            Text(
                text = stringResource(R.string.language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(PaddingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(selectedLanguage.titleRes()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (isVisible) {
            LanguageBottomSheet(
                selected = selectedLanguage,
                onSelect = onSelect,
                onDismiss = onDismiss
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageBottomSheet(
    selected: LanguageType,
    onSelect: (LanguageType) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column {

            LanguageType.entries.forEach { lang ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(lang) }
                        .padding(horizontal = PaddingLarge, vertical = PaddingMedium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(lang.titleRes()),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    RadioButton(
                        selected = selected == lang,
                        onClick = { onSelect(lang) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}