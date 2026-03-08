package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.filters_screen.FiltersEvent
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.filters_screen.FiltersState
import com.example.propertymanagement.ui.filters_screen.FiltersViewModel
import com.example.propertymanagement.ui.map.MapViewModel
import com.example.propertymanagement.ui.map_screen.MapIntent
import com.example.propertymanagement.ui.map_screen.MapState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.androidx.compose.koinViewModel

@Composable
fun FilterScreen(navController: NavController) {

    val filtersViewModel: FiltersViewModel = koinViewModel<FiltersViewModel>()
    val state by filtersViewModel.state.collectAsStateWithLifecycle()
    val intent = filtersViewModel::processIntent
    val event: Flow<FiltersEvent> by remember { mutableStateOf(filtersViewModel.event) }

    LaunchedEffect(Unit) {
        event.filterIsInstance<FiltersEvent>().collect { event ->
            when (event) {
                is FiltersEvent.NavigateBack -> navController.popBackStack()
                FiltersEvent.NavigateToCategorySelection -> navController.navigate(Screens.CategorySelection.route)
            }
        }
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
        CustomFilterTopBar(onCloseClick = { intent(FiltersIntent.NavigateBack) })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CategorySelectorSection(
                onClick = { intent(FiltersIntent.NavigateToCategorySelection) }
            )
        }
    }
}