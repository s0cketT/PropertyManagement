package com.example.propertymanagement.ui.filters_screen.components

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
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.areaDisplayMapper
import com.example.propertymanagement.ui.components.areaValues
import com.example.propertymanagement.ui.components.floorSimpleDisplayMapper
import com.example.propertymanagement.ui.components.floorSimpleValues
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun RoomSectionFilters(
    dealType: DealType?,

    roomsType: RoomsType?,
    roomsForSale: RoomsType?,

    area: IntRangeFilter,
    saleArea: IntRangeFilter,
    kitchenArea: IntRangeFilter,

    bathroomType: BathroomType?,
    ceilingHeight: CeilingHeightType?,
    repairType: ApartmentRepairType?,

    floor: IntRangeFilter,
    floorHouse: IntRangeFilter,

    wallMaterial: WallMaterialType?,

    buildingAmenities: Set<BuildingAmenity>,

    intent: (FiltersIntent) -> Unit
) {
    ExpandableFilterSection(visible = dealType != null) {
        Column {

            EnumTypeSection(
                title = stringResource(R.string.rooms_title),
                entries = RoomsType.entries.toTypedArray(),
                selectedType = roomsType,
                onTypeSelected = { intent(FiltersIntent.RoomsTypeChanged(it)) },
                titleRes = RoomsType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.rooms_for_sale_title),
                entries = RoomsType.entries.toTypedArray(),
                selectedType = roomsForSale,
                onTypeSelected = { intent(FiltersIntent.RoomsForSaleChanged(it)) },
                titleRes = RoomsType::titleRes
            )

            RangeFilterItem(
                titleResId = R.string.area_title,
                values = areaValues,
                displayMapper = areaDisplayMapper,
                range = area,
                onApply = { intent(FiltersIntent.AreaChanged(it)) }
            )

            RangeFilterItem(
                titleResId = R.string.sale_area,
                values = areaValues,
                displayMapper = areaDisplayMapper,
                range = saleArea,
                onApply = { intent(FiltersIntent.SaleAreaChanged(it)) }
            )

            RangeFilterItem(
                titleResId = R.string.kitchen_area_title,
                values = areaValues,
                displayMapper = areaDisplayMapper,
                range = kitchenArea,
                onApply = { intent(FiltersIntent.KitchenAreaChanged(it)) }
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.bathroom_title),
                entries = BathroomType.entries.toTypedArray(),
                selectedType = bathroomType,
                onTypeSelected = { intent(FiltersIntent.BathroomTypeChanged(it)) },
                titleRes = BathroomType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.ceiling_title),
                entries = CeilingHeightType.entries.toTypedArray(),
                selectedType = ceilingHeight,
                onTypeSelected = { intent(FiltersIntent.CeilingHeightChanged(it)) },
                titleRes = CeilingHeightType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.repair_title),
                entries = ApartmentRepairType.entries.toTypedArray(),
                selectedType = repairType,
                onTypeSelected = { intent(FiltersIntent.RepairTypeChanged(it)) },
                titleRes = ApartmentRepairType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            RangeFilterItem(
                titleResId = R.string.floor_title,
                values = floorSimpleValues,
                displayMapper = floorSimpleDisplayMapper,
                range = floor,
                onApply = { intent(FiltersIntent.FloorChanged(it)) }
            )

            RangeFilterItem(
                titleResId = R.string.floor_house_title,
                values = floorSimpleValues,
                displayMapper = floorSimpleDisplayMapper,
                range = floorHouse,
                onApply = { intent(FiltersIntent.FloorHouseChanged(it)) }
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.wall_material_title),
                entries = WallMaterialType.entries.toTypedArray(),
                selectedType = wallMaterial,
                onTypeSelected = { intent(FiltersIntent.WallMaterialChanged(it)) },
                titleRes = WallMaterialType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            AmenitiesFilterItem(
                titleResId = R.string.building_amenities_title,
                items = BuildingAmenity.entries,
                selected = buildingAmenities,
                titleRes = { it.titleRes() },
                onApply = { intent(FiltersIntent.BuildingAmenitiesChanged(it)) }
            )
        }
    }
}