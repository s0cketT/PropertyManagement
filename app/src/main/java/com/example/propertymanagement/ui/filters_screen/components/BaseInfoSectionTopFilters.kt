package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.ui.components.DealTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.FadeAnimatedContent
import com.example.propertymanagement.ui.extensions.orEmptyValue
import com.example.propertymanagement.ui.extensions.priceTitle
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.filters_screen.FiltersState
import com.example.propertymanagement.ui.mapper.titleResDefault
import com.example.propertymanagement.ui.theme.PaddingLarge

@Composable
fun BaseInfoSectionTopFilters(state: FiltersState, intent: (FiltersIntent) -> Unit) {

    Column {
        TypeSelectorSection(
            selectedType = state.selectedPropertyType,
            onClick = { intent(FiltersIntent.NavigateToCategorySelection) },
            onResetClick = { intent(FiltersIntent.ClearPropertyType) }
        )

        RegionSelectorSection(
            selectedRegionName = state.selectedRegionName,
            selectedCityCount = state.selectedCityNames.size,
            onClick = { intent(FiltersIntent.NavigateToRegionSelection) },
            onResetClick = { intent(FiltersIntent.ClearLocationSelection) },
        )

        if (state.selectedPropertyType != null) {
            Spacer(modifier = Modifier.height(PaddingLarge))

            DealTypeSection(
                selectedType = state.dealType,
                onTypeSelected = { intent(FiltersIntent.CommercialDealTypeChanged(it)) },
                titleRes = DealType::titleResDefault
            )
        }

        Spacer(modifier = Modifier.height(PaddingLarge))

        FadeAnimatedContent(state.dealType) { dealType ->
            PriceRangeSection(
                title = stringResource(dealType.priceTitle()),
                priceFrom = state.price.from.orEmptyValue(),
                priceTo = state.price.to.orEmptyValue(),
                selectedCurrency = state.selectedCurrency,
                onPriceFromChange = { intent(FiltersIntent.PriceChanged(state.price.copy(from = it))) },
                onPriceToChange = { intent(FiltersIntent.PriceChanged(state.price.copy(to = it))) },
                onCurrencySelected = { intent(FiltersIntent.CurrencyChanged(it)) }
            )
        }

        ExpandableFilterSection(
            visible = state.dealType != null
        ) {
            Column {
                Spacer(modifier = Modifier.height(PaddingLarge))

                PriceRangeSection(
                    title = stringResource(R.string.price_per_meter),
                    priceFrom = state.pricePerMeter.from.orEmptyValue(),
                    priceTo = state.pricePerMeter.to.orEmptyValue(),
                    selectedCurrency = state.selectedCurrency,
                    onPriceFromChange = { intent(FiltersIntent.PricePerMeterChanged(state.pricePerMeter.copy(from = it))) },
                    onPriceToChange = { intent(FiltersIntent.PricePerMeterChanged(state.pricePerMeter.copy(to = it))) },
                    onCurrencySelected = { intent(FiltersIntent.CurrencyChanged(it)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(PaddingLarge))

        FadeAnimatedContent(state.dealType) { type ->
            SellerTypeSection(
                selectedType = state.sellerType,
                dealType = type,
                onTypeSelected = { type -> intent(FiltersIntent.SellerTypeChanged(type)) },
            )
        }
    }
}