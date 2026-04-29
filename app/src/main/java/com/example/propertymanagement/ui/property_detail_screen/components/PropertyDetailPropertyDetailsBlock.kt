package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.PropertyDetails
import com.example.propertymanagement.ui.mapper.titleRes

@Composable
fun PropertyDetailPropertyDetailsBlock(details: PropertyDetails) {
    when (details) {
        is PropertyDetails.Apartment -> {
            details.isWalkthrough?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.property_detail_walkthrough),
                    value = stringResource(if (it) R.string.answer_yes else R.string.answer_no)
                )
            }
            details.livingArea?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.living_area_title),
                    value = formatDouble(it)
                )
            }
            details.kitchenArea?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.property_detail_kitchen_area),
                    value = formatDouble(it)
                )
            }
            details.bathroomType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.bathroom_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.balconyType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.balcony_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.ceilingHeight?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.ceiling_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.repairType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.repair_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.wallMaterial?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.wall_material_title),
                    value = stringResource(it.titleRes())
                )
            }
            if (details.windowViews.isNotEmpty()) {
                val windowViewsText = buildString {
                    details.windowViews.forEachIndexed { index, windowView ->
                        if (index > 0) {
                            append(", ")
                        }
                        append(stringResource(windowView.titleRes()))
                    }
                }
                PropertyDetailInfoRow(
                    label = stringResource(R.string.window_views_title),
                    value = windowViewsText
                )
            }
        }

        is PropertyDetails.Room -> {
            details.roomsForSale?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.rooms_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.saleArea?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.sale_area),
                    value = formatDouble(it)
                )
            }
            if (details.windowViews.isNotEmpty()) {
                val windowViewsText = buildString {
                    details.windowViews.forEachIndexed { index, windowView ->
                        if (index > 0) {
                            append(", ")
                        }
                        append(stringResource(windowView.titleRes()))
                    }
                }
                PropertyDetailInfoRow(
                    label = stringResource(R.string.window_views_title),
                    value = windowViewsText
                )
            }
        }

        is PropertyDetails.House -> {
            details.houseType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.house_type_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.landArea?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.property_detail_land_area),
                    value = formatDouble(it)
                )
            }
            details.floors?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.property_detail_floors),
                    value = it.toString()
                )
            }
            details.roofType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.roof_type_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.heatingType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.heating_type_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.waterType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.water_type_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.gasType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.gas_type_title),
                    value = stringResource(it.titleRes())
                )
            }
        }

        is PropertyDetails.Garage -> {
            details.heatingType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.heating_type_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.parkingType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.parking_type_title),
                    value = stringResource(it.titleRes())
                )
            }
        }

        is PropertyDetails.Commercial -> {
            details.commercialType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.filter_commercial_type_title),
                    value = stringResource(it.titleRes())
                )
            }
            details.repairType?.let {
                PropertyDetailInfoRow(
                    label = stringResource(R.string.repair_title),
                    value = stringResource(it.titleRes())
                )
            }
        }
    }
}
