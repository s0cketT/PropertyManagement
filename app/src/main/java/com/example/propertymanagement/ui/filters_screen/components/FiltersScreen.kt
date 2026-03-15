package com.example.propertymanagement.ui.filters_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CommercialAmenity
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.ExpandableFilterSection
import com.example.propertymanagement.ui.components.FadeAnimatedContent
import com.example.propertymanagement.ui.components.areaValues
import com.example.propertymanagement.ui.components.areaDisplayMapper
import com.example.propertymanagement.ui.components.floorDisplayMapper
import com.example.propertymanagement.ui.components.floorValues
import com.example.propertymanagement.ui.components.separateRoomsDisplayMapper
import com.example.propertymanagement.ui.components.separateRoomsValues
import com.example.propertymanagement.ui.extensions.orEmptyValue
import com.example.propertymanagement.ui.extensions.priceTitle
import com.example.propertymanagement.ui.filters_screen.FiltersEvent
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.filters_screen.FiltersState
import com.example.propertymanagement.ui.filters_screen.FiltersViewModel
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.theme.BoxGrayHeight
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.PrimaryBlue
import com.example.propertymanagement.ui.theme.TopBarBackground
import com.example.propertymanagement.ui.theme.UnselectedGray
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

    // Преобразуем строку обратно в enum (безопасно)
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

    LaunchedEffect(Unit) {
        event.filterIsInstance<FiltersEvent>().collect { event ->
            when (event) {
                is FiltersEvent.NavigateBack -> navController.popBackStack()
                FiltersEvent.NavigateToCategorySelection -> navController.navigate(Screens.CategorySelection.route)
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
            .background(MaterialTheme.colorScheme.surface)
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
            TypeSelectorSection(
                selectedType = state.selectedPropertyType,
                onClick = { intent(FiltersIntent.NavigateToCategorySelection) },
                onResetClick = { intent(FiltersIntent.ClearPropertyType) }
            )

            if (state.selectedPropertyType == PropertyType.COMMERCIAL) {
                Spacer(modifier = Modifier.height(PaddingLarge))

                CommercialDealTypeSection(
                    selectedType = state.selectedDealType,
                    onTypeSelected = { intent(FiltersIntent.CommercialDealTypeChanged(it)) }
                )
            }

            Spacer(modifier = Modifier.height(PaddingLarge))

            FadeAnimatedContent(state.selectedDealType) { dealType ->

                PriceRangeSection(
                    title = stringResource(dealType.priceTitle()),
                    priceFrom = state.price.from.orEmptyValue(),
                    priceTo = state.price.to.orEmptyValue(),
                    selectedCurrency = state.selectedCurrency,
                    onPriceFromChange = { intent(FiltersIntent.PriceChanged(state.price.copy(from = it))) },
                    onPriceToChange = { intent(FiltersIntent.PriceChanged(state.price.copy(to = it))) },
                    onCurrencySelected = { intent(FiltersIntent.CurrencyChanged(it)) }
                )
            }

            ExpandableFilterSection(
                visible = state.selectedPropertyType == PropertyType.COMMERCIAL &&
                        state.selectedDealType != null
            ) {
                Column {
                    Spacer(modifier = Modifier.height(PaddingLarge))

                    PriceRangeSection(
                        title = stringResource(R.string.price_per_meter),
                        priceFrom = state.pricePerMeter.from.orEmptyValue(),
                        priceTo = state.pricePerMeter.to.orEmptyValue(),
                        selectedCurrency = state.selectedCurrency,
                        onPriceFromChange = { intent(FiltersIntent.PricePerMeterChanged(state.pricePerMeter.copy(from = it))) },
                        onPriceToChange = { intent(FiltersIntent.PricePerMeterChanged(state.pricePerMeter.copy(to = it))) },
                        onCurrencySelected = { intent(FiltersIntent.CurrencyChanged(it)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(PaddingLarge))

            FadeAnimatedContent(state.selectedDealType) { type ->

                SellerTypeSection(
                    selectedType = state.selectedSellerType,
                    dealType = type,
                    onTypeSelected = { type -> intent(FiltersIntent.SellerTypeChanged(type)) },
                )
            }

            ExpandableFilterSection(
                visible = state.selectedPropertyType == PropertyType.COMMERCIAL && state.selectedDealType != null
            ) {
                Column() {
                    CommercialPropertyTypeSection(
                        selectedType = state.selectedCommercialPropertyType,
                        onTypeSelected = { intent(FiltersIntent.CommercialPropertyTypeChanged(it)) }
                    )

                    Spacer(modifier = Modifier.height(PaddingLarge))

                    RangeFilterItem(
                        titleResId = R.string.area_title,
                        values = areaValues,
                        displayMapper = areaDisplayMapper,
                        range = state.area,
                        onApply = { range -> intent(FiltersIntent.AreaChanged(range)) }
                    )

                    RangeFilterItem(
                        titleResId = R.string.floor_title,
                        values = floorValues,
                        displayMapper = floorDisplayMapper,
                        range = state.floor,
                        onApply = { range -> intent(FiltersIntent.FloorChanged(range)) }
                    )

                    RangeFilterItem(
                        titleResId = R.string.floor_house_title,
                        values = floorValues,
                        displayMapper = floorDisplayMapper,
                        range = state.floorHouse,
                        onApply = { range -> intent(FiltersIntent.FloorHouseChanged(range)) }
                    )

                    RangeFilterItem(
                        titleResId = R.string.separate_rooms_title,
                        values = separateRoomsValues,
                        displayMapper = separateRoomsDisplayMapper,
                        range = state.separateRooms,
                        onApply = { range -> intent(FiltersIntent.SeparateRoomsChanged(range)) }
                    )


                    AmenitiesFilterItem(
                        titleResId = R.string.amenities,
                        items = CommercialAmenity.entries,
                        selected = state.commercialAmenities,
                        titleRes = { it.titleRes() },
                        onApply = { intent(FiltersIntent.AmenitiesChanged(it)) }
                    )

                }
            }

            Spacer(modifier = Modifier.height(PaddingLarge))

            OnlyWithPhotosSection(
                isChecked = state.onlyWithPhotos,
                onCheckedChange = { checked -> intent(FiltersIntent.OnlyWithPhotosChanged(checked)) }
            )

            Box(modifier = Modifier.fillMaxWidth().height(BoxGrayHeight).background(TopBarBackground))

            Spacer(modifier = Modifier.height(PaddingLarge))

            SortingSection(
                selectedSort = state.sortType,
                onSortSelected = { intent(FiltersIntent.SortChanged(it)) }
            )
        }

        Spacer(modifier = Modifier.height(PaddingLarge))

        ShowPropertiesButton(
            enabled = state.isFiltersValid,
            onClick = { intent(FiltersIntent.SaveFilters) }
        )

        Spacer(modifier = Modifier.height(PaddingLarge))
    }
}

@Composable
private fun ShowPropertiesButton(
    enabled: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge),
        shape = RoundedCornerShape(ButtonCornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) PrimaryBlue else UnselectedGray,
            contentColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.show_properties),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = PaddingSmall)
        )
    }
}