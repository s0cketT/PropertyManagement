package com.example.propertymanagement.ui.publish_screen.components

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
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.floorSimpleDisplayMapper
import com.example.propertymanagement.ui.components.floorSimpleValues
import com.example.propertymanagement.ui.filters_screen.components.AmenitiesFilterItem
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge

@Composable
fun CommercialSection(
    dealType: DealType?,
    roomsType: RoomsType?,
    commercialType: CommercialPropertyType?,
    amenities: Set<CommercialAmenity>,
    repairType: CommercialRepairType?,
    floor: Int?,
    floorHouse: Int?,

    onCommercialType: (CommercialPropertyType?) -> Unit,
    onRoomsType: (RoomsType?) -> Unit,
    onAmenities: (Set<CommercialAmenity>) -> Unit,
    onRepairType: (CommercialRepairType?) -> Unit,
    onFloor: (Int?) -> Unit,
    onFloorHouse: (Int?) -> Unit
) {
    ExpandableFilterSection(
        visible = dealType != null
    ) {
        Column {

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.filter_commercial_type_title),
                entries = CommercialPropertyType.entries.toTypedArray(),
                selectedType = commercialType,
                onTypeSelected = onCommercialType,
                titleRes = CommercialPropertyType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.rooms_title),
                entries = RoomsType.entries.toTypedArray(),
                selectedType = roomsType,
                onTypeSelected = onRoomsType,
                titleRes = RoomsType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            AmenitiesFilterItem(
                titleResId = R.string.amenities,
                items = CommercialAmenity.entries,
                selected = amenities,
                titleRes = { it.titleRes() },
                onApply = onAmenities
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.filter_repair_type_title),
                entries = CommercialRepairType.entries.toTypedArray(),
                selectedType = repairType,
                onTypeSelected = onRepairType,
                titleRes = CommercialRepairType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            SingleValueFilterItem(
                titleResId = R.string.floor_title,
                value = floor,
                values = floorSimpleValues,
                displayMapper = floorSimpleDisplayMapper,
                onApply = onFloor
            )

            SingleValueFilterItem(
                titleResId = R.string.floor_house_title,
                value = floorHouse,
                values = floorSimpleValues,
                displayMapper = floorSimpleDisplayMapper,
                onApply = onFloorHouse
            )
        }
    }
}