package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.domain.model.GeosuggestItem
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.SpacerTiny

@Composable
fun GeosuggestTextField(
    value: String,
    placeholder: String,
    suggestions: List<GeosuggestItem>,
    isLoading: Boolean,
    onValueChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        PropertyTitleTextField(
            value = value,
            placeholder = placeholder,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = SpacerTiny)
                    .padding(start = 4.dp),
                strokeWidth = 2.dp,
            )
        }

        if (suggestions.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpacerTiny),
                shape = RoundedCornerShape(ButtonCornerRadius),
                tonalElevation = 1.dp,
                shadowElevation = 2.dp,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    suggestions.forEach { suggestion ->
                        GeosuggestSuggestionRow(
                            suggestion = suggestion,
                            onClick = { onSuggestionClick(suggestion.title) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GeosuggestSuggestionRow(
    suggestion: GeosuggestItem,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Text(
            text = suggestion.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        val subtitle = suggestion.subtitle
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}
