package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.publish_screen.PublishState
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.CardElevationLow
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium

@Composable
fun PublishLocationCard(
    state: PublishState,
    onOpenAddressSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val summary = buildLocationSummary(state).ifBlank {
        stringResource(R.string.publish_location_not_set)
    }
    val subtitleColor =
        if (state.isLocationError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge)
            .clickable(onClick = onOpenAddressSheet),
        shape = RoundedCornerShape(ButtonCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = CardElevationLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = PaddingMedium)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.publish_location_section_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleColor
                )
            }
        }
    }
}

private fun buildLocationSummary(state: PublishState): String {
    val lat = state.latitude
    val lon = state.longitude
    if (lat == null || lon == null) {
        return ""
    }
    val parts = listOf(
        state.addressCity,
        state.addressStreet,
        state.addressHouse
    ).filter { it.isNotBlank() }
    if (parts.isNotEmpty()) {
        return parts.joinToString(separator = ", ")
    }
    return listOf(
        state.addressRegion,
        state.addressCountry
    ).filter { it.isNotBlank() }.joinToString(separator = ", ")
}
