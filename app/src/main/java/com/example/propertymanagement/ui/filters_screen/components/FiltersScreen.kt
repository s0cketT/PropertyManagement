package com.example.propertymanagement.ui.filters_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.PrimaryActionButton
import com.example.propertymanagement.ui.filters_screen.FiltersEvent
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.filters_screen.FiltersState
import com.example.propertymanagement.ui.filters_screen.FiltersViewModel
import com.example.propertymanagement.ui.theme.PaddingLarge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.androidx.compose.koinViewModel

@Composable
fun FilterScreen(navController: NavController) {

    val filtersViewModel: FiltersViewModel = koinViewModel<FiltersViewModel>()
    val state by filtersViewModel.state.collectAsStateWithLifecycle()
    val intent = filtersViewModel::processIntent
    val event: Flow<FiltersEvent> by remember { mutableStateOf(filtersViewModel.event) }

    // Получаем значение из SavedStateHandle
    val backStackEntry = navController.currentBackStackEntry!!
    val savedStateHandle = backStackEntry.savedStateHandle

    val selectedTypeName by savedStateHandle
        .getStateFlow<String?>("selected_property_type", null)
        .collectAsStateWithLifecycle()

    val selectedRegionIdRaw by savedStateHandle
        .getStateFlow<String?>("selected_region_id", null)
        .collectAsStateWithLifecycle()

    val selectedRegionName by savedStateHandle
        .getStateFlow<String?>("selected_region_name", null)
        .collectAsStateWithLifecycle()

    val selectedCityIdsRaw by savedStateHandle
        .getStateFlow<String?>("selected_city_ids", null)
        .collectAsStateWithLifecycle()

    val selectedCityNamesRaw by savedStateHandle
        .getStateFlow<String?>("selected_city_names", null)
        .collectAsStateWithLifecycle()

    val selectedLocationLatRaw by savedStateHandle
        .getStateFlow<String?>("selected_location_lat", null)
        .collectAsStateWithLifecycle()

    val selectedLocationLngRaw by savedStateHandle
        .getStateFlow<String?>("selected_location_lng", null)
        .collectAsStateWithLifecycle()

    val selectedType = selectedTypeName?.let { name ->
        try {
            PropertyType.valueOf(name)
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    // Записываем выбранный тип в state один раз при возврате
    LaunchedEffect(selectedType) {
        if (selectedType != null) {
            intent(FiltersIntent.SelectPropertyType(selectedType))
            savedStateHandle.remove<String>("selected_property_type")
        }
    }

    LaunchedEffect(selectedRegionIdRaw, selectedRegionName) {
        val regionId = selectedRegionIdRaw?.toLongOrNull()
        val regionName = selectedRegionName?.trim().orEmpty()

        if (regionId != null && regionName.isNotEmpty()) {
            intent(
                FiltersIntent.SelectRegion(
                    id = regionId,
                    name = regionName,
                )
            )
            savedStateHandle.remove<String>("selected_region_id")
            savedStateHandle.remove<String>("selected_region_name")
        }
    }

    LaunchedEffect(selectedCityIdsRaw, selectedCityNamesRaw) {
        val cityIds = selectedCityIdsRaw
            .orEmpty()
            .split(",")
            .mapNotNull { value -> value.trim().toLongOrNull() }
            .toSet()

        val cityNames = selectedCityNamesRaw
            .orEmpty()
            .split(";;")
            .map { value -> value.trim() }
            .filter { value -> value.isNotEmpty() }
            .toSet()

        if (selectedCityIdsRaw != null || selectedCityNamesRaw != null) {
            intent(
                FiltersIntent.SelectCities(
                    cityIds = cityIds,
                    cityNames = cityNames,
                )
            )
            savedStateHandle.remove<String>("selected_city_ids")
            savedStateHandle.remove<String>("selected_city_names")
        }
    }

    LaunchedEffect(selectedLocationLatRaw, selectedLocationLngRaw) {
        if (selectedLocationLatRaw != null || selectedLocationLngRaw != null) {
            intent(
                FiltersIntent.SelectLocationCoordinate(
                    lat = selectedLocationLatRaw?.toDoubleOrNull(),
                    lng = selectedLocationLngRaw?.toDoubleOrNull(),
                )
            )
            savedStateHandle.remove<String>("selected_location_lat")
            savedStateHandle.remove<String>("selected_location_lng")
        }
    }

    LaunchedEffect(Unit) {
        event.filterIsInstance<FiltersEvent>().collect { event ->
            when (event) {
                is FiltersEvent.NavigateBack -> navController.popBackStack()
                FiltersEvent.NavigateToCategorySelection -> navController.navigate(Screens.CategorySelection.route)
                FiltersEvent.NavigateToRegionSelection -> navController.navigate(Screens.RegionSelection.route)
            }
        }
    }

    BackHandler {
        intent(FiltersIntent.NavigateBack)
    }

    UI(state = state, intent = intent)

}

@Composable
private fun UI(
    state: FiltersState,
    intent: (FiltersIntent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        CustomFilterTopBar(
            onCloseClick = { intent(FiltersIntent.NavigateBack) },
            onClearClick = { intent(FiltersIntent.ClearFilters) }
            )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            BaseInfoSectionTopFilters(state, intent)

            when (state.selectedPropertyType) {
                PropertyType.COMMERCIAL -> CommercialSectionFilters(
                    dealType = state.dealType,
                    commercialPropertyType = state.commercialPropertyType,
                    commercialRepairType = state.commercialRepairType,
                    area = state.area,
                    floor = state.floor,
                    floorHouse = state.floorHouse,
                    commercialAmenities = state.commercialAmenities,
                    intent = intent
                )
                PropertyType.APARTMENT -> ApartmentSectionFilters(
                    dealType = state.dealType,
                    roomsType = state.roomsType,
                    isWalkthroughRoom = state.isWalkthroughRoom,
                    area = state.area,
                    livingArea = state.livingArea,
                    kitchenArea = state.kitchenArea,
                    bathroomType = state.bathroomType,
                    balconyType = state.balconyType,
                    ceilingHeight = state.ceilingHeight,
                    repairType = state.repairType,
                    windowViews = state.windowViews,
                    floor = state.floor,
                    floorHouse = state.floorHouse,
                    wallMaterial = state.wallMaterial,
                    yearBuilt = state.yearBuilt,
                    buildingAmenities = state.buildingAmenities,
                    intent = intent
                )
                PropertyType.ROOM -> RoomSectionFilters(
                    dealType = state.dealType,
                    roomsType = state.roomsType,
                    roomsForSale = state.roomsForSale,
                    area = state.area,
                    saleArea = state.saleArea,
                    kitchenArea = state.kitchenArea,
                    bathroomType = state.bathroomType,
                    ceilingHeight = state.ceilingHeight,
                    repairType = state.repairType,
                    windowViews = state.windowViews,
                    floor = state.floor,
                    floorHouse = state.floorHouse,
                    wallMaterial = state.wallMaterial,
                    buildingAmenities = state.buildingAmenities,
                    intent = intent
                )
                PropertyType.HOUSE -> HouseSectionFilters(
                    dealType = state.dealType,
                    houseType = state.houseType,
                    roomsType = state.roomsType,
                    area = state.area,
                    landArea = state.landArea,
                    livingArea = state.livingArea,
                    kitchenArea = state.kitchenArea,
                    ceilingHeight = state.ceilingHeight,
                    floorHouse = state.floorHouse,
                    wallMaterial = state.wallMaterial,
                    roofType = state.roofType,
                    yearBuilt = state.yearBuilt,
                    heatingType = state.heatingType,
                    waterType = state.waterType,
                    gasType = state.gasType,
                    houseAmenities = state.houseAmenities,
                    intent = intent
                )
                PropertyType.GARAGE -> GarageSectionFilters(
                    dealType = state.dealType,
                    area = state.area,
                    heatingType = state.heatingType,
                    parkingType = state.parkingType,
                    intent = intent
                )
                else -> Unit
            }

            BaseInfoSectionBottomFilters(state, intent)
        }

        Spacer(modifier = Modifier.height(PaddingLarge))

        PrimaryActionButton(
            text = R.string.show_properties,
            textOverride = state.showPropertiesText(),
            containerColor = state.showPropertiesButtonContainerColor(),
            contentColor = state.showPropertiesButtonContentColor(),
            onClick = { intent(FiltersIntent.SaveFilters) },
            enabled = state.isFiltersValid,
        )

        Spacer(modifier = Modifier.height(PaddingLarge))
    }
}

@Composable
private fun FiltersState.showPropertiesText(): String {
    return androidx.compose.ui.res.stringResource(
        R.string.show_properties_with_count,
        matchedPropertiesCount,
    )
}

@Composable
private fun FiltersState.showPropertiesButtonContainerColor() =
    if (matchedPropertiesCount == 0) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primary
    }

@Composable
private fun FiltersState.showPropertiesButtonContentColor() =
    if (matchedPropertiesCount == 0) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }