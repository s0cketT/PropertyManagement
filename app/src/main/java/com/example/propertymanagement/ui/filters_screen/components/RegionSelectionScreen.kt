package com.example.propertymanagement.ui.filters_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.propertymanagement.ui.list_property_screen.components.ErrorState
import com.example.propertymanagement.ui.list_property_screen.components.LoadingState
import com.example.propertymanagement.ui.filters_screen.region_selection.RegionSelectionViewModel
import com.example.propertymanagement.ui.theme.HorizontalPadding
import com.example.propertymanagement.ui.theme.SmallHorizontalPadding
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.TopBarHeight
import com.example.propertymanagement.ui.theme.VerticalPaddingItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegionSelectionScreen(navController: NavController) {

    val viewModel: RegionSelectionViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        SelectionTopBar(
            title = stringResource(R.string.region),
            onBackClick = { navController.popBackStack() },
        )

        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(error = state.error?.message)
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(state.regions) { region ->
                        SelectionListItem(
                            title = region.name,
                            onClick = {
                                val filtersSavedState = navController
                                    .getBackStackEntry(Screens.Filters.route)
                                    .savedStateHandle

                                filtersSavedState["selected_region_id"] = region.id.toString()
                                filtersSavedState["selected_region_name"] = region.name
                                filtersSavedState.remove<String>("selected_city_ids")
                                filtersSavedState.remove<String>("selected_city_names")
                                filtersSavedState.remove<String>("selected_location_lat")
                                filtersSavedState.remove<String>("selected_location_lng")

                                navController.navigate(
                                    Screens.CitySelection.createRoute(
                                        regionId = region.id,
                                        regionName = region.name,
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectionTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .height(TopBarHeight)
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
        )
    }
}

@Composable
private fun SelectionListItem(
    title: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = HorizontalPadding,
                    vertical = VerticalPaddingItem,
                ),
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

