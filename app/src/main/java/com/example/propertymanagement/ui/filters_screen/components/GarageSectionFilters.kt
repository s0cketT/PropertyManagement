package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.areaDisplayMapper
import com.example.propertymanagement.ui.components.areaValues
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun GarageSectionFilters(
    dealType: DealType?,

    area: IntRangeFilter,

    heatingType: HeatingType?,
    parkingType: ParkingType?,

    intent: (FiltersIntent) -> Unit
) {
    ExpandableFilterSection(visible = dealType != null) {
        Column {

            RangeFilterItem(
                titleResId = R.string.area_title,
                values = areaValues,
                displayMapper = areaDisplayMapper,
                range = area,
                onApply = { intent(FiltersIntent.AreaChanged(it)) }
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.heating_type_title),
                entries = HeatingType.entries.toTypedArray(),
                selectedType = heatingType,
                onTypeSelected = { intent(FiltersIntent.HeatingTypeChanged(it)) },
                titleRes = HeatingType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.parking_type_title),
                entries = ParkingType.entries.toTypedArray(),
                selectedType = parkingType,
                onTypeSelected = { intent(FiltersIntent.ParkingTypeChanged(it)) },
                titleRes = ParkingType::titleRes
            )
        }
    }
}