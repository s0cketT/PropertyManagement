package com.example.propertymanagement.ui.filters_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.filters_screen.city_selection.CitySelectionViewModel
import com.example.propertymanagement.ui.list_property_screen.components.ErrorState
import com.example.propertymanagement.ui.list_property_screen.components.LoadingState
import com.example.propertymanagement.ui.theme.HorizontalPadding
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SmallHorizontalPadding
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.TopBarHeight
import com.example.propertymanagement.ui.theme.VerticalPaddingItemSmall
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CitySelectionScreen(
    navController: NavController,
    regionId: Long,
    regionName: String,
) {

    val selectedCityIds = navController.getBackStackEntry(Screens.Filters.route)
        .savedStateHandle
        .get<String>("selected_city_ids")
        .orEmpty()
        .split(",")
        .mapNotNull { it.trim().toLongOrNull() }
        .toSet()

    val viewModel: CitySelectionViewModel = koinViewModel(
        parameters = {
            parametersOf(regionId, selectedCityIds)
        },
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        CitySelectionTopBar(
            title = regionName,
            onBackClick = { navController.popBackStack() },
            onApplyClick = {
                val selectedCities = state.cities.filter { city ->
                    city.id in state.selectedCityIds
                }
                val cityIdsCsv = selectedCities.joinToString(",") { it.id.toString() }
                val cityNamesCsv = selectedCities.joinToString(";;") { it.name }
                val focusCity = selectedCities.firstOrNull { city ->
                    city.lat != null && city.lng != null
                }

                val filtersSavedState = navController.getBackStackEntry(Screens.Filters.route)
                    .savedStateHandle

                filtersSavedState["selected_city_ids"] = cityIdsCsv
                filtersSavedState["selected_city_names"] = cityNamesCsv
                if (focusCity != null) {
                    filtersSavedState["selected_location_lat"] = focusCity.lat.toString()
                    filtersSavedState["selected_location_lng"] = focusCity.lng.toString()
                } else {
                    filtersSavedState.remove<String>("selected_location_lat")
                    filtersSavedState.remove<String>("selected_location_lng")
                }

                navController.popBackStack(Screens.Filters.route, false)
            },
        )

        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(error = state.error!!.message)
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(state.cities) { city ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onCityToggle(city.id) }
                            .padding(
                                horizontal = HorizontalPadding,
                                vertical = VerticalPaddingItemSmall,
                            ),
                        horizontalArrangement = Arrangement.spacedBy(PaddingLarge),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = city.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                        )

                        Checkbox(
                            checked = city.id in state.selectedCityIds,
                            onCheckedChange = { viewModel.onCityToggle(city.id) },
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun CitySelectionTopBar(
    title: String,
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .height(TopBarHeight),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SmallHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(modifier = Modifier.width(SpacerBetweenElements))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )

            Text(
                text = stringResource(R.string.apply_text),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onApplyClick),
            )
        }
    }
}

