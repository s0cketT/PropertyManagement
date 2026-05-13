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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.use_case.GetPropertyDetailPricesUseCase
import com.example.propertymanagement.ui.common.PropertyMultiCurrencyPriceColumn
import com.example.propertymanagement.ui.common.formatPropertyPublicationTime
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.SpacerTiny

@Composable
fun PropertyContent(
    property: Property,
    currencyRates: Map<String, CurrencyRate>,
    managerCommissionPercent: Double = 0.0,
    /** `null` — сначала валюта объявления; иначе приоритетная валюта карточки каталога. */
    cardPriceLeadCurrency: CurrencyType? = null,
) {
    val rooms = property.rooms

    val area = property.area

    val address = listOfNotNull(
        property.region,
        property.city,
        property.street,
        property.house
    ).joinToString(", ")

    val priceUseCase = remember { GetPropertyDetailPricesUseCase() }
    val convertedPrices = remember(
        property.id,
        property.price,
        property.currency,
        currencyRates,
        managerCommissionPercent,
    ) {
        priceUseCase(property, currencyRates, managerCommissionPercent)
    }

    val locale = LocalConfiguration.current.locales[0]
    val publishedAt = remember(property.createdAt, locale) {
        formatPropertyPublicationTime(property.createdAt, locale)
    }

    Column(
        modifier = Modifier.padding(PaddingLarge)
    ) {

        Text(
            text = property.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2
        )

        publishedAt?.let {
            Spacer(modifier = Modifier.height(SpacerTiny))
            Text(
                text = stringResource(R.string.list_property_published_at, it),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(SpacerTiny))

        PropertyMultiCurrencyPriceColumn(
            property = property,
            convertedPrices = convertedPrices,
            compact = true,
            primaryBold = false,
            managerCommissionPercent = managerCommissionPercent,
            priceLeadCurrency = cardPriceLeadCurrency,
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
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            area?.let {
                Text(
                    text = stringResource(R.string.area_value, it),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (property.floor != null && property.totalFloors != null) {
                Text(
                    text = "${property.floor}/${property.totalFloors}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (address.isNotEmpty()) {
            Spacer(modifier = Modifier.height(SpacerSmall))

            Text(
                text = address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
