package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetailPrices
import com.example.propertymanagement.ui.common.formatPropertyPublicationTime
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.mapper.titleResListingDetail
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.SpacerLarge
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.SpacerTiny

@Composable
fun PropertyDetailInfoSections(
    property: Property,
    convertedPrices: PropertyDetailPrices?,
    managerCommissionPercent: Double = 0.0,
    showBuyerCommissionCaption: Boolean = true,
    /** Каталог: ведущая валюта (фильтр / USD). `null` — сначала валюта объявления. */
    cardPriceLeadCurrency: CurrencyType? = null,
    nearbyMapPois: List<NearbyMapPoi> = emptyList(),
    visiblePoiCategories: Set<NearbyPoiCategory> = emptySet(),
    onSetPoiCategoryVisible: (NearbyPoiCategory, Boolean) -> Unit = { _, _ -> },
    isNearbyPoisLoading: Boolean = false,
    nearbyPoisLoadFailed: Boolean = false,
    onOpenMapFullscreen: () -> Unit,
    onSubmitRequest: (() -> Unit)?,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PropertyDetailPriceBlock(
            property = property,
            convertedPrices = convertedPrices,
            managerCommissionPercent = managerCommissionPercent,
            showBuyerCommissionCaption = showBuyerCommissionCaption,
            cardPriceLeadCurrency = cardPriceLeadCurrency,
        )

        Spacer(modifier = Modifier.height(SpacerTiny))

        Text(
            text = property.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )

        val addressLine = buildPropertyDetailAddressLine(property)
        if (addressLine.isNotBlank()) {
            Text(
                text = addressLine,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = SpacerTiny)
            )
        }

        val locale = LocalConfiguration.current.locales[0]
        val publishedFormatted = remember(property.createdAt, locale) {
            formatPropertyPublicationTime(property.createdAt, locale)
        }
        publishedFormatted?.let {
            Text(
                text = stringResource(R.string.property_detail_published_at, it),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
                modifier = Modifier.padding(top = SpacerTiny)
            )
        }

        PropertyDetailSectionHeader(title = stringResource(R.string.property_detail_section_main))

        PropertyDetailInfoRow(
            label = stringResource(R.string.property_detail_property_type),
            value = stringResource(property.type.titleRes())
        )
        PropertyDetailInfoRow(
            label = stringResource(R.string.property_detail_deal_type),
            value = stringResource(property.dealType.titleResListingDetail())
        )

        val areaForLayout = effectiveArea(property)
        val hasAreaBlock = areaForLayout != null ||
            property.rooms != null ||
            property.floor != null ||
            property.yearBuilt != null

        if (hasAreaBlock) {
            PropertyDetailSectionDivider()
            PropertyDetailSectionHeader(title = stringResource(R.string.property_detail_section_area))
            areaForLayout?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.property_detail_area),
                    value = formatDouble(it)
                )
            }
            property.rooms?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.rooms_title),
                    value = stringResource(it.titleRes())
                )
            }
            property.floor?.let { fl ->
                val tf = property.totalFloors
                val floorText = if (tf != null) {
                    stringResource(R.string.floor_value, fl.toString(), tf.toString())
                } else {
                    fl.toString()
                }
                PropertyDetailInfoRow(
                    label = stringResource(R.string.floor_title),
                    value = floorText
                )
            }
            property.yearBuilt?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.year_built),
                    value = it.toString()
                )
            }
        }

        property.details?.let { details ->
            PropertyDetailSectionDivider()
            PropertyDetailSectionHeader(title = stringResource(R.string.property_detail_section_details))
            PropertyDetailPropertyDetailsBlock(details = details)
        }

        val amenityLabels =
            property.buildingAmenities.map { stringResource(it.titleRes()) } +
                property.houseAmenities.map { stringResource(it.titleRes()) } +
                property.commercialAmenities.map { stringResource(it.titleRes()) }
        if (amenityLabels.isNotEmpty()) {
            PropertyDetailSectionDivider()
            PropertyDetailSectionHeader(title = stringResource(R.string.property_detail_section_amenities))
            PropertyDetailAmenityChips(labels = amenityLabels)
        }

        PropertyDetailSectionDivider()
        PropertyDetailSectionHeader(title = stringResource(R.string.property_detail_section_description))
        val descriptionBody = property.description?.trim().takeIf { !it.isNullOrBlank() }
        Text(
            text = descriptionBody
                ?: stringResource(R.string.property_detail_description_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = if (descriptionBody != null) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
            },
            modifier = Modifier.padding(top = SpacerTiny)
        )

        PropertyDetailSectionDivider()
        PropertyDetailSectionHeader(title = stringResource(R.string.property_detail_section_location))

        property.country?.takeIf { it.isNotBlank() }?.let {
            PropertyDetailInfoRow(
                label = stringResource(R.string.property_detail_country),
                value = it
            )
        }
        property.region?.takeIf { it.isNotBlank() }?.let {
            PropertyDetailInfoRow(
                label = stringResource(R.string.property_detail_region),
                value = it
            )
        }
        property.city?.takeIf { it.isNotBlank() }?.let {
            PropertyDetailInfoRow(
                label = stringResource(R.string.property_detail_city),
                value = it
            )
        }
        property.street?.takeIf { it.isNotBlank() }?.let {
            PropertyDetailInfoRow(
                label = stringResource(R.string.property_detail_street),
                value = it
            )
        }
        property.house?.takeIf { it.isNotBlank() }?.let {
            PropertyDetailInfoRow(
                label = stringResource(R.string.property_detail_house),
                value = it
            )
        }

        Spacer(modifier = Modifier.height(SpacerSmall))

        PropertyDetailMapSection(
            property = property,
            nearbyMapPois = nearbyMapPois,
            visiblePoiCategories = visiblePoiCategories,
            onSetPoiCategoryVisible = onSetPoiCategoryVisible,
            isNearbyPoisLoading = isNearbyPoisLoading,
            nearbyPoisLoadFailed = nearbyPoisLoadFailed,
            onOpenFullscreen = onOpenMapFullscreen,
        )

        onSubmitRequest?.let { submit ->
            Spacer(modifier = Modifier.height(SpacerLarge))

            Button(
                onClick = submit,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ButtonCornerRadius),
            ) {
                Text(text = stringResource(R.string.property_detail_submit_request))
            }
        }
    }
}
