package com.example.propertymanagement.ui.publish_screen.components

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
import com.example.propertymanagement.ui.extensions.priceTitle
import com.example.propertymanagement.ui.mapper.titleResPublish
import com.example.propertymanagement.ui.publish_screen.PublishIntent
import com.example.propertymanagement.ui.publish_screen.PublishState
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun BaseInfoSection(state: PublishState, intent: (PublishIntent) -> Unit) {

    ExpandableFilterSection(
        visible = state.propertyType != null
    ) {
        Column {

            DealTypeSection(
                selectedType = state.dealType,
                onTypeSelected = { intent(PublishIntent.SetDealType(it)) },
                titleRes = DealType::titleResPublish
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            SingleValueFilterItem(
                titleResId = R.string.area_title,
                value = state.area,
                onApply = { intent(PublishIntent.SetArea(it)) }
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            FadeAnimatedContent(state.dealType) { dealType ->
                PriceSection(
                    title = stringResource(dealType.priceTitle()),
                    price = state.price,
                    currency = state.currency,
                    area = state.area,
                    isError = state.isPriceError,
                    onPriceChange = { intent(PublishIntent.SetPrice(it)) },
                    onCurrencySelected = { intent(PublishIntent.SetCurrency(it)) }
                )
            }
        }
    }
}