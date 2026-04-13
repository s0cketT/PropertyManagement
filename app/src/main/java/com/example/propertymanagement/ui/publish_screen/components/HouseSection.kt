package com.example.propertymanagement.ui.publish_screen.components

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
import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.domain.model.WallMaterialType
import com.example.propertymanagement.domain.model.WaterType
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.floorSimpleDisplayMapper
import com.example.propertymanagement.ui.components.floorSimpleValues
import com.example.propertymanagement.ui.components.yearDisplayMapper
import com.example.propertymanagement.ui.components.yearValues
import com.example.propertymanagement.ui.filters_screen.components.AmenitiesFilterItem
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge

@Composable
fun HouseSection(
    dealType: DealType?,
    houseType: HouseType?,
    landArea: Int?,
    livingArea: Int?,
    kitchenArea: Int?,
    ceilingHeight: CeilingHeightType?,
    wallMaterial: WallMaterialType?,
    floorHouse: Int?,
    yearBuilt: Int?,
    roofType: RoofType?,
    heatingType: HeatingType?,
    houseAmenities: Set<HouseAmenity>,
    waterType: WaterType?,
    gasType: GasType?,

    onHouseType: (HouseType?) -> Unit,
    onLandArea: (Int?) -> Unit,
    onLivingArea: (Int?) -> Unit,
    onKitchenArea: (Int?) -> Unit,
    onCeilingHeight: (CeilingHeightType?) -> Unit,
    onWallMaterial: (WallMaterialType?) -> Unit,
    onFloorHouse: (Int?) -> Unit,
    onYearBuilt: (Int?) -> Unit,
    onRoofType: (RoofType?) -> Unit,
    onHeatingType: (HeatingType?) -> Unit,
    onHouseAmenities: (Set<HouseAmenity>) -> Unit,
    onWaterType: (WaterType?) -> Unit,
    onGasType: (GasType?) -> Unit
) {
    ExpandableFilterSection(visible = dealType != null) {
        Column {

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.house_type_title),
                entries = HouseType.entries.toTypedArray(),
                selectedType = houseType,
                onTypeSelected = onHouseType,
                titleRes = HouseType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            SingleValueFilterItem(
                titleResId = R.string.land_area,
                value = landArea,
                onApply = onLandArea,
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
                title = stringResource(R.string.wall_material_title),
                entries = WallMaterialType.entries.toTypedArray(),
                selectedType = wallMaterial,
                onTypeSelected = onWallMaterial,
                titleRes = WallMaterialType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            SingleValueFilterItem(
                titleResId = R.string.floor_house_title,
                value = floorHouse,
                values = floorSimpleValues,
                displayMapper = floorSimpleDisplayMapper,
                onApply = onFloorHouse
            )

            SingleValueFilterItem(
                titleResId = R.string.year_built,
                value = yearBuilt,
                values = yearValues,
                displayMapper = yearDisplayMapper,
                onApply = onYearBuilt
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.roof_type_title),
                entries = RoofType.entries.toTypedArray(),
                selectedType = roofType,
                onTypeSelected = onRoofType,
                titleRes = RoofType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.heating_type_title),
                entries = HeatingType.entries.toTypedArray(),
                selectedType = heatingType,
                onTypeSelected = onHeatingType,
                titleRes = HeatingType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            AmenitiesFilterItem(
                titleResId = R.string.house_amenities,
                items = HouseAmenity.entries,
                selected = houseAmenities,
                titleRes = { it.titleRes() },
                onApply = onHouseAmenities
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.water_type_title),
                entries = WaterType.entries.toTypedArray(),
                selectedType = waterType,
                onTypeSelected = onWaterType,
                titleRes = WaterType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.gas_type_title),
                entries = GasType.entries.toTypedArray(),
                selectedType = gasType,
                onTypeSelected = onGasType,
                titleRes = GasType::titleRes
            )
        }
    }
}