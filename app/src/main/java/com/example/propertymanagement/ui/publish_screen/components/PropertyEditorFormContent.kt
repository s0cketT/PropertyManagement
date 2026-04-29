package com.example.propertymanagement.ui.publish_screen.components

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
import com.example.propertymanagement.ui.filters_screen.components.TypeSelectorSection
import com.example.propertymanagement.ui.property_detail_screen.components.PropertyDetailSectionDivider
import com.example.propertymanagement.ui.publish_screen.PublishIntent
import com.example.propertymanagement.ui.publish_screen.PublishState
import com.example.propertymanagement.ui.publish_screen.buildAddressQueryString
import com.example.propertymanagement.ui.publish_screen.geocodeAddressQuery
import com.example.propertymanagement.ui.publish_screen.reverseGeocodeCoordinates
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import kotlinx.coroutines.launch

@Composable
fun PropertyEditorFormContent(
    state: PublishState,
    intent: (PublishIntent) -> Unit,
    topBarTitleRes: Int,
    submitButtonRes: Int,
) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        AppTopBar(
            title = topBarTitleRes,
            onBackClick = { intent(PublishIntent.NavigateBack) },
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
                onValueChange = { intent(PublishIntent.SetTitle(it)) },
                modifier = Modifier.padding(horizontal = PaddingLarge),
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            PublishLocationCard(
                state = state,
                onOpenAddressSheet = { intent(PublishIntent.SetAddressBottomSheetOpen(true)) },
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            TypeSelectorSection(
                selectedType = state.propertyType,
                onClick = { intent(PublishIntent.OpenCategorySelection) },
                onResetClick = { intent(PublishIntent.ClearPropertyType) },
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            BaseInfoSection(state, intent)

            when (state.propertyType) {
                PropertyType.COMMERCIAL -> CommercialSection(
                    dealType = state.dealType,
                    roomsType = state.roomsType,
                    commercialType = state.selectedCommercialPropertyType,
                    amenities = state.commercialAmenities,
                    repairType = state.selectedCommercialRepairType,
                    floor = state.floor,
                    floorHouse = state.floorHouse,

                    onCommercialType = { intent(PublishIntent.SetCommercialPropertyType(it)) },
                    onRoomsType = { intent(PublishIntent.SetRoomsType(it)) },
                    onAmenities = { intent(PublishIntent.SetCommercialAmenities(it)) },
                    onRepairType = { intent(PublishIntent.SetCommercialRepairType(it)) },
                    onFloor = { intent(PublishIntent.SetFloor(it)) },
                    onFloorHouse = { intent(PublishIntent.SetFloorHouse(it)) },
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
                    windowViews = state.windowViews,
                    wallMaterial = state.wallMaterial,

                    onRoomsType = { intent(PublishIntent.SetRoomsType(it)) },
                    onLivingArea = { intent(PublishIntent.SetLivingArea(it)) },
                    onKitchenArea = { intent(PublishIntent.SetKitchenArea(it)) },
                    onBalconyType = { intent(PublishIntent.SetBalconyType(it)) },
                    onBathroomType = { intent(PublishIntent.SetBathroomType(it)) },
                    onWalkthrough = { intent(PublishIntent.SetWalkthroughRoom(it)) },
                    onCeilingHeight = { intent(PublishIntent.SetCeilingHeight(it)) },
                    onFloor = { intent(PublishIntent.SetFloor(it)) },
                    onFloorHouse = { intent(PublishIntent.SetFloorHouse(it)) },
                    onRepairType = { intent(PublishIntent.SetRepairType(it)) },
                    onYearBuilt = { intent(PublishIntent.SetYearBuilt(it)) },
                    onBuildingAmenities = { intent(PublishIntent.SetBuildingAmenities(it)) },
                    onWindowViews = { intent(PublishIntent.SetWindowViews(it)) },
                    onWallMaterial = { intent(PublishIntent.SetWallMaterial(it)) },
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
                    windowViews = state.windowViews,
                    wallMaterial = state.wallMaterial,

                    onRoomsType = { intent(PublishIntent.SetRoomsType(it)) },
                    onSaleArea = { intent(PublishIntent.SetSaleArea(it)) },
                    onRoomsForSale = { intent(PublishIntent.SetRoomsForSaleType(it)) },
                    onKitchenArea = { intent(PublishIntent.SetKitchenArea(it)) },
                    onCeilingHeight = { intent(PublishIntent.SetCeilingHeight(it)) },
                    onBathroomType = { intent(PublishIntent.SetBathroomType(it)) },
                    onFloor = { intent(PublishIntent.SetFloor(it)) },
                    onFloorHouse = { intent(PublishIntent.SetFloorHouse(it)) },
                    onRepairType = { intent(PublishIntent.SetRepairType(it)) },
                    onBuildingAmenities = { intent(PublishIntent.SetBuildingAmenities(it)) },
                    onWindowViews = { intent(PublishIntent.SetWindowViews(it)) },
                    onWallMaterial = { intent(PublishIntent.SetWallMaterial(it)) },
                )
                PropertyType.HOUSE -> HouseSection(
                    dealType = state.dealType,
                    roomsType = state.roomsType,
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

                    onHouseType = { intent(PublishIntent.SetHouseType(it)) },
                    onRoomsType = { intent(PublishIntent.SetRoomsType(it)) },
                    onLandArea = { intent(PublishIntent.SetLandArea(it)) },
                    onLivingArea = { intent(PublishIntent.SetLivingArea(it)) },
                    onKitchenArea = { intent(PublishIntent.SetKitchenArea(it)) },
                    onCeilingHeight = { intent(PublishIntent.SetCeilingHeight(it)) },
                    onWallMaterial = { intent(PublishIntent.SetWallMaterial(it)) },
                    onFloorHouse = { intent(PublishIntent.SetFloorHouse(it)) },
                    onYearBuilt = { intent(PublishIntent.SetYearBuilt(it)) },
                    onRoofType = { intent(PublishIntent.SetRoofType(it)) },
                    onHeatingType = { intent(PublishIntent.SetHeatingType(it)) },
                    onHouseAmenities = { intent(PublishIntent.SetHouseAmenities(it)) },
                    onWaterType = { intent(PublishIntent.SetWaterType(it)) },
                    onGasType = { intent(PublishIntent.SetGasType(it)) },
                )
                PropertyType.GARAGE -> GarageSection(
                    dealType = state.dealType,
                    heatingType = state.heatingType,
                    parkingType = state.parkingType,

                    onHeatingType = { intent(PublishIntent.SetHeatingType(it)) },
                    onParkingType = { intent(PublishIntent.SetParkingType(it)) },
                )
                else -> Unit
            }

            PropertyDetailSectionDivider()

            OutlinedTextField(
                value = state.description,
                onValueChange = { intent(PublishIntent.SetDescription(it)) },
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
            onClick = { intent(PublishIntent.Submit) },
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
                onDismiss = { intent(PublishIntent.SetAddressBottomSheetOpen(false)) },
                onConfirmAddress = {
                    scope.launch {
                        val query = state.buildAddressQueryString()
                        if (query.isBlank()) {
                            intent(PublishIntent.AddressSheetDone(null, null))
                            return@launch
                        }
                        val coords = geocodeAddressQuery(query)
                        intent(
                            PublishIntent.AddressSheetDone(
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
                onDismiss = { intent(PublishIntent.SetMapPickerOpen(false)) },
                onConfirm = { lat, lon ->
                    scope.launch {
                        val parts = reverseGeocodeCoordinates(lat, lon)
                        intent(
                            PublishIntent.ConfirmMapLocation(
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
