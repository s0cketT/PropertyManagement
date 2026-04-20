package com.example.propertymanagement.ui.edit_property_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyIntent
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyState
import com.example.propertymanagement.ui.edit_property_screen.buildAddressQueryString
import com.example.propertymanagement.ui.filters_screen.components.TypeSelectorSection
import com.example.propertymanagement.ui.property_detail_screen.components.PropertyDetailSectionDivider
import com.example.propertymanagement.ui.publish_screen.components.ApartmentSection
import com.example.propertymanagement.ui.publish_screen.components.BaseInfoSection
import com.example.propertymanagement.ui.publish_screen.components.CommercialSection
import com.example.propertymanagement.ui.publish_screen.components.GarageSection
import com.example.propertymanagement.ui.publish_screen.components.HouseSection
import com.example.propertymanagement.ui.publish_screen.components.ImagePickerSection
import com.example.propertymanagement.ui.publish_screen.components.PropertyTitleTextField
import com.example.propertymanagement.ui.publish_screen.components.PublishAddressBottomSheet
import com.example.propertymanagement.ui.publish_screen.components.PublishLocationCard
import com.example.propertymanagement.ui.publish_screen.components.PublishMapPickerDialog
import com.example.propertymanagement.ui.publish_screen.components.RoomSection
import com.example.propertymanagement.ui.publish_screen.geocodeAddressQuery
import com.example.propertymanagement.ui.publish_screen.reverseGeocodeCoordinates
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import kotlinx.coroutines.launch

@Composable
fun EditPropertyFormContent(
    state: EditPropertyState,
    intent: (EditPropertyIntent) -> Unit,
    topBarTitleRes: Int,
    submitButtonRes: Int,
) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        AppTopBar(
            title = topBarTitleRes,
            onBackClick = { intent(EditPropertyIntent.NavigateBack) },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {

            Spacer(modifier = Modifier.height(SpacerMedium))

            ImagePickerSection(state, intent)

            PropertyTitleTextField(
                value = state.title,
                placeholder = stringResource(R.string.property_title_placeholder),
                isError = state.isTitleError,
                onValueChange = { intent(EditPropertyIntent.SetTitle(it)) },
                modifier = Modifier.padding(horizontal = PaddingLarge),
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PublishLocationCard(
                state = state,
                onOpenAddressSheet = { intent(EditPropertyIntent.SetAddressBottomSheetOpen(true)) },
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            TypeSelectorSection(
                selectedType = state.propertyType,
                onClick = { intent(EditPropertyIntent.OpenCategorySelection) },
                onResetClick = { intent(EditPropertyIntent.ClearPropertyType) },
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            BaseInfoSection(state, intent)

            when (state.propertyType) {
                PropertyType.COMMERCIAL -> CommercialSection(
                    dealType = state.dealType,
                    commercialType = state.selectedCommercialPropertyType,
                    amenities = state.commercialAmenities,
                    repairType = state.selectedCommercialRepairType,
                    floor = state.floor,
                    floorHouse = state.floorHouse,

                    onCommercialType = { intent(EditPropertyIntent.SetCommercialPropertyType(it)) },
                    onAmenities = { intent(EditPropertyIntent.SetCommercialAmenities(it)) },
                    onRepairType = { intent(EditPropertyIntent.SetCommercialRepairType(it)) },
                    onFloor = { intent(EditPropertyIntent.SetFloor(it)) },
                    onFloorHouse = { intent(EditPropertyIntent.SetFloorHouse(it)) },
                )
                PropertyType.APARTMENT -> ApartmentSection(
                    dealType = state.dealType,
                    roomsType = state.roomsType,
                    livingArea = state.livingArea,
                    kitchenArea = state.kitchenArea,
                    balconyType = state.balconyType,
                    bathroomType = state.bathroomType,
                    isWalkthroughRoom = state.isWalkthroughRoom,
                    ceilingHeight = state.ceilingHeight,
                    floor = state.floor,
                    floorHouse = state.floorHouse,
                    repairType = state.repairType,
                    yearBuilt = state.yearBuilt,
                    buildingAmenities = state.buildingAmenities,
                    wallMaterial = state.wallMaterial,

                    onRoomsType = { intent(EditPropertyIntent.SetRoomsType(it)) },
                    onLivingArea = { intent(EditPropertyIntent.SetLivingArea(it)) },
                    onKitchenArea = { intent(EditPropertyIntent.SetKitchenArea(it)) },
                    onBalconyType = { intent(EditPropertyIntent.SetBalconyType(it)) },
                    onBathroomType = { intent(EditPropertyIntent.SetBathroomType(it)) },
                    onWalkthrough = { intent(EditPropertyIntent.SetWalkthroughRoom(it)) },
                    onCeilingHeight = { intent(EditPropertyIntent.SetCeilingHeight(it)) },
                    onFloor = { intent(EditPropertyIntent.SetFloor(it)) },
                    onFloorHouse = { intent(EditPropertyIntent.SetFloorHouse(it)) },
                    onRepairType = { intent(EditPropertyIntent.SetRepairType(it)) },
                    onYearBuilt = { intent(EditPropertyIntent.SetYearBuilt(it)) },
                    onBuildingAmenities = { intent(EditPropertyIntent.SetBuildingAmenities(it)) },
                    onWallMaterial = { intent(EditPropertyIntent.SetWallMaterial(it)) },
                )
                PropertyType.ROOM -> RoomSection(
                    dealType = state.dealType,
                    roomsType = state.roomsType,
                    saleArea = state.saleArea,
                    roomsForSale = state.roomsForSaleType,
                    kitchenArea = state.kitchenArea,
                    ceilingHeight = state.ceilingHeight,
                    bathroomType = state.bathroomType,
                    floor = state.floor,
                    floorHouse = state.floorHouse,
                    repairType = state.repairType,
                    buildingAmenities = state.buildingAmenities,
                    wallMaterial = state.wallMaterial,

                    onRoomsType = { intent(EditPropertyIntent.SetRoomsType(it)) },
                    onSaleArea = { intent(EditPropertyIntent.SetSaleArea(it)) },
                    onRoomsForSale = { intent(EditPropertyIntent.SetRoomsForSaleType(it)) },
                    onKitchenArea = { intent(EditPropertyIntent.SetKitchenArea(it)) },
                    onCeilingHeight = { intent(EditPropertyIntent.SetCeilingHeight(it)) },
                    onBathroomType = { intent(EditPropertyIntent.SetBathroomType(it)) },
                    onFloor = { intent(EditPropertyIntent.SetFloor(it)) },
                    onFloorHouse = { intent(EditPropertyIntent.SetFloorHouse(it)) },
                    onRepairType = { intent(EditPropertyIntent.SetRepairType(it)) },
                    onBuildingAmenities = { intent(EditPropertyIntent.SetBuildingAmenities(it)) },
                    onWallMaterial = { intent(EditPropertyIntent.SetWallMaterial(it)) },
                )
                PropertyType.HOUSE -> HouseSection(
                    dealType = state.dealType,
                    houseType = state.houseType,
                    landArea = state.landArea,
                    livingArea = state.livingArea,
                    kitchenArea = state.kitchenArea,
                    ceilingHeight = state.ceilingHeight,
                    wallMaterial = state.wallMaterial,
                    floorHouse = state.floorHouse,
                    yearBuilt = state.yearBuilt,
                    roofType = state.roofType,
                    heatingType = state.heatingType,
                    houseAmenities = state.houseAmenities,
                    waterType = state.waterType,
                    gasType = state.gasType,

                    onHouseType = { intent(EditPropertyIntent.SetHouseType(it)) },
                    onLandArea = { intent(EditPropertyIntent.SetLandArea(it)) },
                    onLivingArea = { intent(EditPropertyIntent.SetLivingArea(it)) },
                    onKitchenArea = { intent(EditPropertyIntent.SetKitchenArea(it)) },
                    onCeilingHeight = { intent(EditPropertyIntent.SetCeilingHeight(it)) },
                    onWallMaterial = { intent(EditPropertyIntent.SetWallMaterial(it)) },
                    onFloorHouse = { intent(EditPropertyIntent.SetFloorHouse(it)) },
                    onYearBuilt = { intent(EditPropertyIntent.SetYearBuilt(it)) },
                    onRoofType = { intent(EditPropertyIntent.SetRoofType(it)) },
                    onHeatingType = { intent(EditPropertyIntent.SetHeatingType(it)) },
                    onHouseAmenities = { intent(EditPropertyIntent.SetHouseAmenities(it)) },
                    onWaterType = { intent(EditPropertyIntent.SetWaterType(it)) },
                    onGasType = { intent(EditPropertyIntent.SetGasType(it)) },
                )
                PropertyType.GARAGE -> GarageSection(
                    dealType = state.dealType,
                    heatingType = state.heatingType,
                    parkingType = state.parkingType,

                    onHeatingType = { intent(EditPropertyIntent.SetHeatingType(it)) },
                    onParkingType = { intent(EditPropertyIntent.SetParkingType(it)) },
                )
                else -> Unit
            }

            PropertyDetailSectionDivider()

            OutlinedTextField(
                value = state.description,
                onValueChange = { intent(EditPropertyIntent.SetDescription(it)) },
                modifier = Modifier
                    .padding(horizontal = PaddingLarge)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.publish_description_placeholder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
                    )
                },
                minLines = 3,
                maxLines = 10,
                shape = RoundedCornerShape(ButtonCornerRadius),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(SpacerMedium))
        }

        Button(
            onClick = { intent(EditPropertyIntent.Submit) },
            enabled = !state.isPublishing,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingLarge),
            shape = RoundedCornerShape(ButtonCornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        ) {
            Text(stringResource(submitButtonRes))
        }

        if (state.isAddressBottomSheetOpen) {
            PublishAddressBottomSheet(
                state = state,
                intent = intent,
                onDismiss = { intent(EditPropertyIntent.SetAddressBottomSheetOpen(false)) },
                onConfirmAddress = {
                    scope.launch {
                        val query = state.buildAddressQueryString()
                        if (query.isBlank()) {
                            intent(EditPropertyIntent.AddressSheetDone(null, null))
                            return@launch
                        }
                        val coords = geocodeAddressQuery(query)
                        intent(
                            EditPropertyIntent.AddressSheetDone(
                                latitude = coords?.first,
                                longitude = coords?.second,
                            ),
                        )
                    }
                },
            )
        }

        if (state.isMapPickerOpen) {
            PublishMapPickerDialog(
                initialLatitude = state.latitude,
                initialLongitude = state.longitude,
                onDismiss = { intent(EditPropertyIntent.SetMapPickerOpen(false)) },
                onConfirm = { lat, lon ->
                    scope.launch {
                        val parts = reverseGeocodeCoordinates(lat, lon)
                        intent(
                            EditPropertyIntent.ConfirmMapLocation(
                                latitude = lat,
                                longitude = lon,
                                geocoded = parts,
                            ),
                        )
                    }
                },
            )
        }
    }
}
