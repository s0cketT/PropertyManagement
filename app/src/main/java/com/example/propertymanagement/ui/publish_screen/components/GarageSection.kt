package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.ui.components.EnumTypeSection
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun GarageSection(
    dealType: DealType?,
    heatingType: HeatingType?,
    parkingType: ParkingType?,

    onHeatingType: (HeatingType?) -> Unit,
    onParkingType: (ParkingType?) -> Unit
) {
    ExpandableFilterSection(
        visible = dealType != null
    ) {
        Column {

            Spacer(modifier = Modifier.height(SpacerMedium))

            EnumTypeSection(
                title = stringResource(R.string.heating_type_title),
                entries = HeatingType.entries.toTypedArray(),
                selectedType = heatingType,
                onTypeSelected = onHeatingType,
                titleRes = HeatingType::titleRes
            )

            Spacer(modifier = Modifier.height(PaddingLarge))

            EnumTypeSection(
                title = stringResource(R.string.parking_type_title),
                entries = ParkingType.entries.toTypedArray(),
                selectedType = parkingType,
                onTypeSelected = onParkingType,
                titleRes = ParkingType::titleRes
            )
        }
    }
}