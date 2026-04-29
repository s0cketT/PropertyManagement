package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.ApartmentRepairType
import com.example.propertymanagement.domain.model.BathroomType
import com.example.propertymanagement.domain.model.BuildingAmenity
import com.example.propertymanagement.domain.model.CeilingHeightType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WindowViewType
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.floorSimpleDisplayMapper
import com.example.propertymanagement.ui.components.floorSimpleValues
import com.example.propertymanagement.ui.filters_screen.components.AmenitiesFilterItem
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge

@Composable
fun RoomSection(
    dealType: DealType?,
    roomsType: RoomsType?,
    saleArea: Int?,
    roomsForSale: RoomsType?,
    kitchenArea: Int?,
    ceilingHeight: CeilingHeightType?,
    bathroomType: BathroomType?,
    floor: Int?,
    floorHouse: Int?,
    repairType: ApartmentRepairType?,
    buildingAmenities: Set<BuildingAmenity>,
    windowViews: Set<WindowViewType>,
    wallMaterial: WallMaterialType?,

    onRoomsType: (RoomsType?) -> Unit,
    onSaleArea: (Int?) -> Unit,
    onRoomsForSale: (RoomsType?) -> Unit,
    onKitchenArea: (Int?) -> Unit,
    onCeilingHeight: (CeilingHeightType?) -> Unit,
    onBathroomType: (BathroomType?) -> Unit,
    onFloor: (Int?) -> Unit,
    onFloorHouse: (Int?) -> Unit,
    onRepairType: (ApartmentRepairType?) -> Unit,
    onBuildingAmenities: (Set<BuildingAmenity>) -> Unit,
    onWindowViews: (Set<WindowViewType>) -> Unit,
    onWallMaterial: (WallMaterialType?) -> Unit
) {
    ExpandableFilterSection(
        visible = dealType != null
    ) {
        Column {

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.rooms_title),
                entries = RoomsType.entries.toTypedArray(),
                selectedType = roomsType,
                onTypeSelected = onRoomsType,
                titleRes = RoomsType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            SingleValueFilterItem(
                titleResId = R.string.sale_area,
                value = saleArea,
                onApply = onSaleArea,
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.rooms_for_sale_title),
                entries = RoomsType.entries.toTypedArray(),
                selectedType = roomsForSale,
                onTypeSelected = onRoomsForSale,
                titleRes = RoomsType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            SingleValueFilterItem(
                titleResId = R.string.kitchen_area_title,
                value = kitchenArea,
                onApply = onKitchenArea
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.ceiling_title),
                entries = CeilingHeightType.entries.toTypedArray(),
                selectedType = ceilingHeight,
                onTypeSelected = onCeilingHeight,
                titleRes = CeilingHeightType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.bathroom_title),
                entries = BathroomType.entries.toTypedArray(),
                selectedType = bathroomType,
                onTypeSelected = onBathroomType,
                titleRes = BathroomType::titleRes
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

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.repair_title),
                entries = ApartmentRepairType.entries.toTypedArray(),
                selectedType = repairType,
                onTypeSelected = onRepairType,
                titleRes = ApartmentRepairType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            AmenitiesFilterItem(
                titleResId = R.string.building_amenities_title,
                items = BuildingAmenity.entries,
                selected = buildingAmenities,
                titleRes = { it.titleRes() },
                onApply = onBuildingAmenities
            )

            AmenitiesFilterItem(
                titleResId = R.string.window_views_title,
                items = WindowViewType.entries,
                selected = windowViews,
                titleRes = { it.titleRes() },
                onApply = onWindowViews
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

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