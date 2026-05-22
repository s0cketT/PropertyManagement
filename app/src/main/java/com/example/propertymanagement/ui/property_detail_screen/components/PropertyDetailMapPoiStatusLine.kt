package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.NearbyMapPoi

@Composable
internal fun PropertyDetailMapPoiStatusLine(
    nearbyMapPois: List<NearbyMapPoi>,
    visiblePois: List<NearbyMapPoi>,
    isNearbyPoisLoading: Boolean,
    nearbyPoisLoadFailed: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        when {
            isNearbyPoisLoading ->
                Text(
                    text = stringResource(R.string.property_detail_nearby_pois_loading),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

            nearbyPoisLoadFailed ->
                Text(
                    text = stringResource(R.string.property_detail_nearby_pois_error),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )

            nearbyMapPois.isEmpty() ->
                Text(
                    text = stringResource(R.string.property_detail_nearby_pois_empty_area),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

            visiblePois.isNotEmpty() ->
                Text(
                    text = stringResource(
                        R.string.property_detail_nearby_pois_shown,
                        visiblePois.size,
                        nearbyMapPois.size,
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                )
        }
    }
}
