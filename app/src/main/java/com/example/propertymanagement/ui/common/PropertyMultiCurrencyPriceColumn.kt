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
import com.example.propertymanagement.domain.pricing.ManagerCommissionPricing
import com.example.propertymanagement.domain.pricing.formatCommissionPercentForDisplay
import com.example.propertymanagement.ui.mapper.symbol
import com.example.propertymanagement.ui.theme.SpacerTiny

/**
 * @param managerCommissionPercent — надбавка к [Property.price] для отображения покупателю (из БД).
 * @param priceLeadCurrency если задана — первая строка в этой валюте (каталог: USD или валюта сортировки);
 *   если `null` — сначала валюта объявления, ниже остальные («Мои объявления» и экран деталей).
 */
@Composable
fun PropertyMultiCurrencyPriceColumn(
    property: Property,
    convertedPrices: PropertyDetailPrices?,
    compact: Boolean,
    modifier: Modifier = Modifier,
    primaryBold: Boolean = !compact,
    managerCommissionPercent: Double = 0.0,
    showBuyerCommissionCaption: Boolean = true,
    priceLeadCurrency: CurrencyType? = null,
) {
    val listingCurrency = property.currency
    val primaryStyle =
        if (compact) MaterialTheme.typography.titleLarge
        else MaterialTheme.typography.headlineSmall
    val secondaryStyle =
        if (compact) MaterialTheme.typography.bodySmall
        else MaterialTheme.typography.bodyMedium

    Column(modifier = modifier) {
        val buyerGross = ManagerCommissionPricing.grossListingAmount(
            listedPrice = property.price,
            commissionPercent = managerCommissionPercent,
        )

        if (priceLeadCurrency == null) {
            val primarySymbol = listingCurrency.symbol()
            Text(
                text = "${formatPrice(buyerGross)} $primarySymbol",
                style = primaryStyle,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = if (primaryBold) FontWeight.Bold else FontWeight.Normal,
            )
        } else {
            convertedPrices?.let { prices ->
                val leadAmount = when (priceLeadCurrency) {
                    CurrencyType.USD -> prices.usd
                    CurrencyType.EUR -> prices.eur
                    CurrencyType.BYN -> prices.byn
                }
                val leadSymbol = priceLeadCurrency.symbol()
                Text(
                    text = "${formatPrice(leadAmount)} $leadSymbol",
                    style = primaryStyle,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = if (primaryBold) FontWeight.Bold else FontWeight.Normal,
                )
            } ?: Text(
                text = "${formatPrice(buyerGross)} ${listingCurrency.symbol()}",
                style = primaryStyle,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = if (primaryBold) FontWeight.Bold else FontWeight.Normal,
            )
        }

        convertedPrices?.let { prices ->
            val secondaryOrder = if (priceLeadCurrency == null) {
                listOf(CurrencyType.USD, CurrencyType.EUR, CurrencyType.BYN)
                    .filter { it != listingCurrency }
            } else {
                listOf(CurrencyType.USD, CurrencyType.EUR, CurrencyType.BYN)
                    .filter { it != priceLeadCurrency }
            }

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

        if (showBuyerCommissionCaption && managerCommissionPercent > 0.0) {
            Spacer(modifier = Modifier.height(SpacerTiny))
            Text(
                text = stringResource(
                    R.string.buyer_price_includes_manager_commission,
                    formatCommissionPercentForDisplay(managerCommissionPercent),
                ),
                style = secondaryStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
            )
        }
    }
}
