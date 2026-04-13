package com.example.propertymanagement.ui.publish_screen.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.filters_screen.components.TypeSelectorSection
import com.example.propertymanagement.ui.publish_screen.PublishEvent
import com.example.propertymanagement.ui.publish_screen.PublishIntent
import com.example.propertymanagement.ui.publish_screen.PublishState
import com.example.propertymanagement.ui.publish_screen.PublishViewModel
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun PublishScreen(navController: NavController) {

    val publishViewModel: PublishViewModel = koinViewModel<PublishViewModel>()
    val state by publishViewModel.state.collectAsStateWithLifecycle()
    val intent = publishViewModel::processIntent
    val event: Flow<PublishEvent> by remember { mutableStateOf(publishViewModel.event) }

    val context = LocalContext.current
    val message = stringResource(R.string.auth_required)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val imageBytes = uris.mapNotNull { uri ->
            try {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            } catch (e: Exception) {
                null // игнорируем ошибки
            }
        }

        intent(PublishIntent.ImagesSelectedBytes(imageBytes))
    }

    val backStackEntry = navController.currentBackStackEntry!!
    val savedStateHandle = backStackEntry.savedStateHandle

    val selectedTypeName by savedStateHandle
        .getStateFlow<String?>("selected_property_type", null)
        .collectAsStateWithLifecycle()

    val selectedType = selectedTypeName?.let {
        try {
            PropertyType.valueOf(it)
        } catch (_: Exception) {
            null
        }
    }

    LaunchedEffect(selectedType) {
        if (selectedType != null) {
            intent(PublishIntent.SetPropertyType(selectedType))
            savedStateHandle.remove<String>("selected_property_type")
        }
    }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is PublishEvent.NavigateToCategorySelection -> { navController.navigate(Screens.CategorySelection.route) }

                is PublishEvent.OpenGallery -> { launcher.launch("image/*") }

                is PublishEvent.NavigateBack -> { navController.popBackStack() }

                is PublishEvent.ShowAuthRequired -> {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                is PublishEvent.ShowValidationError -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    UI(state, intent)
}

@Preview
@Composable
private fun UI(
    state: PublishState = PublishState(),
    intent: (PublishIntent) -> Unit = {}
) {

    Column(modifier = Modifier.fillMaxSize()) {

        AppTopBar(
            title = R.string.new_ad_title,
            onBackClick = { intent(PublishIntent.NavigateBack) }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(SpacerMedium))

            ImagePickerSection(state, intent)

            PropertyTitleTextField(
                value = state.title,
                placeholder = stringResource(R.string.property_title_placeholder),
                isError = state.isTitleError,
                onValueChange = { intent(PublishIntent.SetTitle(it)) },
                modifier = Modifier.padding(horizontal = PaddingLarge)
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            TypeSelectorSection(
                selectedType = state.propertyType,
                onClick = { intent(PublishIntent.OpenCategorySelection) },
                onResetClick = { intent(PublishIntent.ClearPropertyType) }
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

                    onCommercialType = { intent(PublishIntent.SetCommercialPropertyType(it)) },
                    onAmenities = { intent(PublishIntent.SetCommercialAmenities(it)) },
                    onRepairType = { intent(PublishIntent.SetCommercialRepairType(it)) },
                    onFloor = { intent(PublishIntent.SetFloor(it)) },
                    onFloorHouse = { intent(PublishIntent.SetFloorHouse(it)) }
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
                    onWallMaterial = { intent(PublishIntent.SetWallMaterial(it)) }
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
                    onWallMaterial = { intent(PublishIntent.SetWallMaterial(it)) }
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

                    onHouseType = { intent(PublishIntent.SetHouseType(it)) },
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
                    onGasType = { intent(PublishIntent.SetGasType(it)) }
                )
                PropertyType.GARAGE -> GarageSection(
                    dealType = state.dealType,
                    heatingType = state.heatingType,
                    parkingType = state.parkingType,

                    onHeatingType = { intent(PublishIntent.SetHeatingType(it)) },
                    onParkingType = { intent(PublishIntent.SetParkingType(it)) }
                )
                else -> Unit
            }


        }

        Button(
            onClick =  { intent(PublishIntent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingLarge),
            shape = RoundedCornerShape(ButtonCornerRadius)
        ) {
            Text(stringResource(R.string.publish_button))
        }
    }
}