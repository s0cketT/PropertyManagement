package com.example.propertymanagement.ui.list_property_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetails
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.OnPrimary
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.SpacerTiny

@Composable
fun PropertyContent(property: Property) {

    val details = property.details

    val rooms = property.rooms

    val area = when (details) {
        is PropertyDetails.Apartment -> details.livingArea ?: property.area
        is PropertyDetails.Room -> details.saleArea ?: property.area
        is PropertyDetails.House -> details.landArea ?: property.area
        else -> property.area
    }

    val address = listOfNotNull(
        property.region,
        property.city,
        property.street,
        property.house
    ).joinToString(", ")

    Column(
        modifier = Modifier.padding(PaddingLarge)
    ) {

        Text(
            text = property.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(SpacerTiny))

        Text(
            text = "${property.price} ${property.currency}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            rooms?.let {
                Text(
                    text = stringResource(
                        R.string.rooms_value,
                        stringResource(it.titleRes())
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnPrimary
                )
            }

            area?.let {
                Text(
                    text = stringResource(R.string.area_value, it),
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnPrimary
                )
            }

            if (property.floor != null && property.totalFloors != null) {
                Text(
                    text = "${property.floor}/${property.totalFloors}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnPrimary
                )
            }
        }

        if (address.isNotEmpty()) {
            Spacer(modifier = Modifier.height(SpacerSmall))

            Text(
                text = address,
                style = MaterialTheme.typography.bodySmall,
                color = OnPrimary
            )
        }

        property.description?.let {
            Spacer(modifier = Modifier.height(SpacerSmall))

            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}