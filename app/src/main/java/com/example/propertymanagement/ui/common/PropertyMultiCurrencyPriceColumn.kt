package com.example.propertymanagement.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetailPrices
import com.example.propertymanagement.ui.mapper.symbol
import com.example.propertymanagement.ui.theme.SpacerTiny

/**
 * @param compact — для карточек списка (меньшие стили); false — экран деталей.
 */
@Composable
fun PropertyMultiCurrencyPriceColumn(
    property: Property,
    convertedPrices: PropertyDetailPrices?,
    compact: Boolean,
    modifier: Modifier = Modifier,
    primaryBold: Boolean = !compact
) {
    val listingCurrency = property.currency
    val primarySymbol = listingCurrency.symbol()
    val primaryStyle =
        if (compact) MaterialTheme.typography.titleLarge
        else MaterialTheme.typography.headlineSmall
    val secondaryStyle =
        if (compact) MaterialTheme.typography.bodySmall
        else MaterialTheme.typography.bodyMedium

    Column(modifier = modifier) {
        Text(
            text = "${formatPrice(property.price)} $primarySymbol",
            style = primaryStyle,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = if (primaryBold) FontWeight.Bold else FontWeight.Normal
        )

        convertedPrices?.let { prices ->
            val secondaryOrder = listOf(CurrencyType.USD, CurrencyType.EUR, CurrencyType.BYN)
                .filter { it != listingCurrency }

            if (secondaryOrder.isNotEmpty()) {
                Spacer(modifier = Modifier.height(SpacerTiny))
                secondaryOrder.forEach { currency ->
                    val amount = when (currency) {
                        CurrencyType.USD -> prices.usd
                        CurrencyType.EUR -> prices.eur
                        CurrencyType.BYN -> prices.byn
                    }
                    val sym = currency.symbol()
                    Text(
                        text = stringResource(
                            R.string.property_detail_price_approx,
                            formatPrice(amount),
                            sym
                        ),
                        style = secondaryStyle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
