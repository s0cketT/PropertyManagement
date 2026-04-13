package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.ApartmentRepairType
import com.example.propertymanagement.domain.model.BalconyType
import com.example.propertymanagement.domain.model.BathroomType
import com.example.propertymanagement.domain.model.BuildingAmenity
import com.example.propertymanagement.domain.model.CeilingHeightType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.LabeledCheckboxSection
import com.example.propertymanagement.ui.components.floorSimpleDisplayMapper
import com.example.propertymanagement.ui.components.floorSimpleValues
import com.example.propertymanagement.ui.components.yearDisplayMapper
import com.example.propertymanagement.ui.components.yearValues
import com.example.propertymanagement.ui.filters_screen.components.AmenitiesFilterItem
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun ApartmentSection(
    dealType: DealType?,
    roomsType: RoomsType?,
    livingArea: Int?,
    kitchenArea: Int?,
    balconyType: BalconyType?,
    bathroomType: BathroomType?,
    isWalkthroughRoom: Boolean,
    ceilingHeight: CeilingHeightType?,
    floor: Int?,
    floorHouse: Int?,
    repairType: ApartmentRepairType?,
    yearBuilt: Int?,
    buildingAmenities: Set<BuildingAmenity>,
    wallMaterial: WallMaterialType?,

    onRoomsType: (RoomsType?) -> Unit,
    onLivingArea: (Int?) -> Unit,
    onKitchenArea: (Int?) -> Unit,
    onBalconyType: (BalconyType?) -> Unit,
    onBathroomType: (BathroomType?) -> Unit,
    onWalkthrough: (Boolean) -> Unit,
    onCeilingHeight: (CeilingHeightType?) -> Unit,
    onFloor: (Int?) -> Unit,
    onFloorHouse: (Int?) -> Unit,
    onRepairType: (ApartmentRepairType?) -> Unit,
    onYearBuilt: (Int?) -> Unit,
    onBuildingAmenities: (Set<BuildingAmenity>) -> Unit,
    onWallMaterial: (WallMaterialType?) -> Unit
) {
    ExpandableFilterSection(visible = dealType != null) {
        Column {
            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.rooms_title),
                entries = RoomsType.entries.toTypedArray(),
                selectedType = roomsType,
                onTypeSelected = onRoomsType,
                titleRes = RoomsType::titleRes
            )

            SingleValueFilterItem(
                titleResId = R.string.living_area_title,
                value = livingArea,
                onApply = onLivingArea
            )

            SingleValueFilterItem(
                titleResId = R.string.kitchen_area_title,
                value = kitchenArea,
                onApply = onKitchenArea
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.balcony_title),
                entries = BalconyType.entries.toTypedArray(),
                selectedType = balconyType,
                onTypeSelected = onBalconyType,
                titleRes = BalconyType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.bathroom_title),
                entries = BathroomType.entries.toTypedArray(),
                selectedType = bathroomType,
                onTypeSelected = onBathroomType,
                titleRes = BathroomType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            LabeledCheckboxSection(
                text = stringResource(R.string.is_walkthrough_room),
                isChecked = isWalkthroughRoom,
                onCheckedChange = onWalkthrough
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.ceiling_title),
                entries = CeilingHeightType.entries.toTypedArray(),
                selectedType = ceilingHeight,
                onTypeSelected = onCeilingHeight,
                titleRes = CeilingHeightType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

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

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.repair_title),
                entries = ApartmentRepairType.entries.toTypedArray(),
                selectedType = repairType,
                onTypeSelected = onRepairType,
                titleRes = ApartmentRepairType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            SingleValueFilterItem(
                titleResId = R.string.year_built,
                value = yearBuilt,
                values = yearValues,
                displayMapper = yearDisplayMapper,
                onApply = onYearBuilt
            )

            AmenitiesFilterItem(
                titleResId = R.string.building_amenities_title,
                items = BuildingAmenity.entries,
                selected = buildingAmenities,
                titleRes = { it.titleRes() },
                onApply = onBuildingAmenities
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.wall_material_title),
                entries = WallMaterialType.entries.toTypedArray(),
                selectedType = wallMaterial,
                onTypeSelected = onWallMaterial,
                titleRes = WallMaterialType::titleRes
            )
        }
    }
}