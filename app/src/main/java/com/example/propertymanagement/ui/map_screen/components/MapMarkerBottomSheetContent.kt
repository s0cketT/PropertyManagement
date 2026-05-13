package com.example.propertymanagement.ui.map_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Garage
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.use_case.GetPropertyDetailPricesUseCase
import com.example.propertymanagement.ui.common.PropertyMultiCurrencyPriceColumn
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.mapper.titleResListingDetail
import com.example.propertymanagement.ui.property_detail_screen.components.buildPropertyDetailAddressLine
import com.example.propertymanagement.ui.property_detail_screen.components.effectiveArea
import com.example.propertymanagement.ui.property_detail_screen.components.formatDouble
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.IconSizeStarRow
import com.example.propertymanagement.ui.theme.IconSmall
import com.example.propertymanagement.ui.theme.ImagePickerHeight
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.example.propertymanagement.ui.theme.SpacerHeightSection
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.SpacerTiny
import com.example.propertymanagement.ui.theme.Spacing12
import com.example.propertymanagement.ui.theme.SurfaceTonalElevationLow
import com.example.propertymanagement.ui.theme.VerticalPaddingItem
import com.example.propertymanagement.ui.list_property_screen.components.PropertyImagePager

@Composable
fun MapMarkerBottomSheetContent(
    property: Property,
    currencyRates: Map<String, CurrencyRate>,
    managerCommissionPercent: Double = 0.0,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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

    val addressLine = buildPropertyDetailAddressLine(property)
    val area = effectiveArea(property)

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PaddingLarge, vertical = SpacerSmall)
    ) {
        PropertyImagePager(
            photos = property.photos,
            modifier = Modifier
                .fillMaxWidth()
                .height(ImagePickerHeight)
        )

        Spacer(modifier = Modifier.height(SpacerSmall))

        DealAndPropertyTypeCard(
            dealType = property.dealType,
            propertyType = property.type
        )

        Spacer(modifier = Modifier.height(SpacerSmall))

        Text(
            text = property.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(SpacerTiny))

        PropertyMultiCurrencyPriceColumn(
            property = property,
            convertedPrices = convertedPrices,
            compact = true,
            primaryBold = true,
            managerCommissionPercent = managerCommissionPercent,
        )

        if (area != null || property.rooms != null) {
            Spacer(modifier = Modifier.height(SpacerSmall))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(SpacerSmall))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PaddingLarge)
            ) {
                area?.let {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.property_detail_area),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatDouble(it),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                property.rooms?.let { rooms ->
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.rooms_title),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(rooms.titleRes()),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        if (addressLine.isNotBlank()) {
            Spacer(modifier = Modifier.height(SpacerSmall))
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(SpacerTiny)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Map,
                    contentDescription = null,
                    modifier = Modifier.size(IconSmall),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = addressLine,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(SpacerSmall))

        Button(
            onClick = onDetailsClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(ButtonCornerRadius)
        ) {
            Text(text = stringResource(R.string.map_marker_sheet_details))
        }

        Spacer(modifier = Modifier.height(SpacerSmall))
    }
}

/**
 * Карточка: «Тип сделки» + значение, «Тип недвижимости» + значение (как подписи к полям).
 */
@Composable
private fun DealAndPropertyTypeCard(
    dealType: DealType,
    propertyType: PropertyType
) {
    val dealLabel = stringResource(R.string.property_detail_deal_type)
    val dealValue = stringResource(dealType.titleResListingDetail())
    val typeLabel = stringResource(R.string.property_detail_property_type)
    val typeValue = stringResource(propertyType.titleRes())

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PropertyDetailMapCornerRadius),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = SurfaceTonalElevationLow
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = IconSmall,
                vertical = VerticalPaddingItem
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing12)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Payments,
                    contentDescription = null,
                    modifier = Modifier.size(IconSizeStarRow),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dealLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(SpacerTiny))
                    Text(
                        text = dealValue,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpacerHeightSection))
            HorizontalDivider(
                thickness = DividerThickness,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(SpacerHeightSection))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing12)
            ) {
                Icon(
                    imageVector = propertyType.mapSheetIcon(),
                    contentDescription = null,
                    modifier = Modifier.size(IconSizeStarRow),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(SpacerTiny))
                    Text(
                        text = typeValue,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun PropertyType.mapSheetIcon(): ImageVector = when (this) {
    PropertyType.APARTMENT -> Icons.Outlined.Apartment
    PropertyType.HOUSE -> Icons.Outlined.Home
    PropertyType.COMMERCIAL -> Icons.Outlined.Storefront
    PropertyType.GARAGE -> Icons.Outlined.Garage
    PropertyType.ROOM -> Icons.Outlined.Hotel
}
