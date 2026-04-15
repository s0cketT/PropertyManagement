package com.example.propertymanagement.ui.list_property_screen.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.list_property_screen.ListPropertyEvent
import com.example.propertymanagement.ui.list_property_screen.ListPropertyIntent
import com.example.propertymanagement.ui.list_property_screen.ListPropertyState
import com.example.propertymanagement.ui.property.ListPropertyViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListPropertyScreen(navController: NavController) {

    val listPropertyViewModel: ListPropertyViewModel = koinViewModel()
    val state by listPropertyViewModel.state.collectAsStateWithLifecycle()
    val intent = listPropertyViewModel::processIntent
    val event: Flow<ListPropertyEvent> by remember { mutableStateOf(listPropertyViewModel.event) }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is ListPropertyEvent.NavigateBack -> navController.popBackStack()
                is ListPropertyEvent.NavigateToDetail -> {
                    navController.navigate(
                        Screens.PropertyDetailScreen.createRoute(
                            propertyId = event.property.id,
                            userId = event.userId
                        )
                    )
                }

                is ListPropertyEvent.NavigateToFilterScreen -> { navController.navigate(Screens.Filters.route) }

                is ListPropertyEvent.ShowAuthRequired -> {
                    Toast.makeText(
                        navController.context,
                        navController.context.getString(R.string.auth_required),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    BackHandler {
        intent(ListPropertyIntent.NavigateBack)
    }

    UI(
        state = state,
        intent = intent
    )
}

@Composable
private fun UI(
    state: ListPropertyState,
    intent: (ListPropertyIntent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TopBar(
            query = state.searchQuery,
            onQueryChange = { intent(ListPropertyIntent.OnSearchChanged(it)) },
            onFilterClick = { intent(ListPropertyIntent.OnFilterClick) }
        )

        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(state.error)
            else -> PropertyList(
                list = state.propertiesFilter,
                currencyRates = state.currencyRates,
                onFavoriteClick = { intent(ListPropertyIntent.ToggleFavorite(it)) },
                onItemClick = { intent(ListPropertyIntent.OnPropertyClick(it)) }
                )
        }
    }
}
