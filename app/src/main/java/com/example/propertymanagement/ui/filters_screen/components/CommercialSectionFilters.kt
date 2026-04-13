package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.domain.model.CommercialRepairType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.areaDisplayMapper
import com.example.propertymanagement.ui.components.areaValues
import com.example.propertymanagement.ui.components.floorDisplayMapper
import com.example.propertymanagement.ui.components.floorValues
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge

@Composable
fun CommercialSectionFilters(
    dealType: DealType?,

    commercialPropertyType: CommercialPropertyType?,
    commercialRepairType: CommercialRepairType?,

    area: IntRangeFilter,
    floor: IntRangeFilter,
    floorHouse: IntRangeFilter,

    commercialAmenities: Set<CommercialAmenity>,

    intent: (FiltersIntent) -> Unit
) {
    ExpandableFilterSection(visible = dealType != null) {
        Column {

            EnumTypeSection(
                title = stringResource(R.string.filter_commercial_type_title),
                entries = CommercialPropertyType.entries.toTypedArray(),
                selectedType = commercialPropertyType,
                onTypeSelected = { intent(FiltersIntent.CommercialPropertyTypeChanged(it)) },
                titleRes = CommercialPropertyType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            RangeFilterItem(
                titleResId = R.string.area_title,
                values = areaValues,
                displayMapper = areaDisplayMapper,
                range = area,
                onApply = { intent(FiltersIntent.AreaChanged(it)) }
            )

            AmenitiesFilterItem(
                titleResId = R.string.amenities,
                items = CommercialAmenity.entries,
                selected = commercialAmenities,
                titleRes = { it.titleRes() },
                onApply = { intent(FiltersIntent.AmenitiesChanged(it)) }
            )

            EnumTypeSection(
                title = stringResource(R.string.filter_repair_type_title),
                entries = CommercialRepairType.entries.toTypedArray(),
                selectedType = commercialRepairType,
                onTypeSelected = { intent(FiltersIntent.CommercialRepairTypeChanged(it)) },
                titleRes = CommercialRepairType::titleRes
            )

            RangeFilterItem(
                titleResId = R.string.floor_title,
                values = floorValues,
                displayMapper = floorDisplayMapper,
                range = floor,
                onApply = { intent(FiltersIntent.FloorChanged(it)) }
            )

            RangeFilterItem(
                titleResId = R.string.floor_house_title,
                values = floorValues,
                displayMapper = floorDisplayMapper,
                range = floorHouse,
                onApply = { intent(FiltersIntent.FloorHouseChanged(it)) }
            )
        }
    }
}