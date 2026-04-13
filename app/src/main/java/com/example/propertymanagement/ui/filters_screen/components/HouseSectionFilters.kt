package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CeilingHeightType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.GasType
import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.domain.model.HouseAmenity
import com.example.propertymanagement.domain.model.HouseType
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WaterType
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.areaDisplayMapper
import com.example.propertymanagement.ui.components.areaValues
import com.example.propertymanagement.ui.components.floorSimpleDisplayMapper
import com.example.propertymanagement.ui.components.floorSimpleValues
import com.example.propertymanagement.ui.components.yearDisplayMapper
import com.example.propertymanagement.ui.components.yearValues
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.publish_screen.components.SingleValueFilterItem
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun HouseSectionFilters(
    dealType: DealType?,

    houseType: HouseType?,
    roomsType: RoomsType?,

    area: IntRangeFilter,
    landArea: IntRangeFilter,
    livingArea: IntRangeFilter,
    kitchenArea: IntRangeFilter,

    ceilingHeight: CeilingHeightType?,
    floorHouse: IntRangeFilter,

    wallMaterial: WallMaterialType?,
    roofType: RoofType?,
    yearBuilt: Int?,

    heatingType: HeatingType?,
    waterType: WaterType?,
    gasType: GasType?,

    houseAmenities: Set<HouseAmenity>,

    intent: (FiltersIntent) -> Unit
) {
    ExpandableFilterSection(visible = dealType != null) {
        Column {

            EnumTypeSection(
                title = stringResource(R.string.house_type_title),
                entries = HouseType.entries.toTypedArray(),
                selectedType = houseType,
                onTypeSelected = { intent(FiltersIntent.HouseTypeChanged(it)) },
                titleRes = HouseType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            RangeFilterItem(
                titleResId = R.string.area_title,
                values = areaValues,
                displayMapper = areaDisplayMapper,
                range = area,
                onApply = { intent(FiltersIntent.AreaChanged(it)) }
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.rooms_title),
                entries = RoomsType.entries.toTypedArray(),
                selectedType = roomsType,
                onTypeSelected = { intent(FiltersIntent.RoomsTypeChanged(it)) },
                titleRes = RoomsType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            RangeFilterItem(
                titleResId = R.string.land_area,
                values = areaValues,
                displayMapper = areaDisplayMapper,
                range = landArea,
                onApply = { intent(FiltersIntent.LandAreaChanged(it)) }
            )

            RangeFilterItem(
                titleResId = R.string.living_area_title,
                values = areaValues,
                displayMapper = areaDisplayMapper,
                range = livingArea,
                onApply = { intent(FiltersIntent.LivingAreaChanged(it)) }
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
                title = stringResource(R.string.ceiling_title),
                entries = CeilingHeightType.entries.toTypedArray(),
                selectedType = ceilingHeight,
                onTypeSelected = { intent(FiltersIntent.CeilingHeightChanged(it)) },
                titleRes = CeilingHeightType::titleRes
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

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

            EnumTypeSection(
                title = stringResource(R.string.roof_type_title),
                entries = RoofType.entries.toTypedArray(),
                selectedType = roofType,
                onTypeSelected = { intent(FiltersIntent.RoofTypeChanged(it)) },
                titleRes = RoofType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            SingleValueFilterItem(
                titleResId = R.string.year_built,
                value = yearBuilt,
                values = yearValues,
                displayMapper = yearDisplayMapper,
                onApply = { intent(FiltersIntent.YearBuiltChanged(it)) }
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.heating_type_title),
                entries = HeatingType.entries.toTypedArray(),
                selectedType = heatingType,
                onTypeSelected = { intent(FiltersIntent.HeatingTypeChanged(it)) },
                titleRes = HeatingType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.water_type_title),
                entries = WaterType.entries.toTypedArray(),
                selectedType = waterType,
                onTypeSelected = { intent(FiltersIntent.WaterTypeChanged(it)) },
                titleRes = WaterType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.gas_type_title),
                entries = GasType.entries.toTypedArray(),
                selectedType = gasType,
                onTypeSelected = { intent(FiltersIntent.GasTypeChanged(it)) },
                titleRes = GasType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            AmenitiesFilterItem(
                titleResId = R.string.house_amenities,
                items = HouseAmenity.entries,
                selected = houseAmenities,
                titleRes = { it.titleRes() },
                onApply = { intent(FiltersIntent.HouseAmenitiesChanged(it)) }
            )
        }
    }
}