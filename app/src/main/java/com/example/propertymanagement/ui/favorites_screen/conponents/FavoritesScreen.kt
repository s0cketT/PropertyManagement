package com.example.propertymanagement.ui.favorites_screen.conponents

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.favorites_screen.FavoriteEvent
import com.example.propertymanagement.ui.favorites_screen.FavoriteIntent
import com.example.propertymanagement.ui.favorites_screen.FavoriteState
import com.example.propertymanagement.ui.favorites_screen.FavoriteViewModel
import com.example.propertymanagement.ui.list_property_screen.components.ErrorState
import com.example.propertymanagement.ui.list_property_screen.components.LoadingState
import com.example.propertymanagement.ui.list_property_screen.components.PropertyList
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(navController: NavController) {

    val favoriteViewModel: FavoriteViewModel = koinViewModel()
    val state by favoriteViewModel.state.collectAsStateWithLifecycle()
    val intent = favoriteViewModel::processIntent
    val event: Flow<FavoriteEvent> by remember { mutableStateOf(favoriteViewModel.event) }


    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is FavoriteEvent.NavigateBack -> { navController.popBackStack() }
                FavoriteEvent.NavigateToLogin -> {
                    navController.navigate(Screens.AuthLoginScreen.route)
                }
                is FavoriteEvent.NavigateToDetail -> {
                    navController.navigate(
                        Screens.PropertyDetailScreen.createRoute(
                            propertyId = event.property.id,
                            userId = event.userId
                        )
                    )
                }
                is FavoriteEvent.ShowAuthRequired -> {
                    Toast.makeText(
                        navController.context,
                        navController.context.getString(R.string.auth_required),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    UI(
        state = state,
        intent = intent
    )
}

@Composable
private fun UI(
    state: FavoriteState,
    intent: (FavoriteIntent) -> Unit
) {
    Column() {
        AppTopBar(
            title = R.string.favorite_title,
            onBackClick = { intent(FavoriteIntent.NavigateBack) }
        )

        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(state.error)
            state.currentUserId == null -> {
                FavoritesGuestLoginPrompt(
                    onLoginClick = { intent(FavoriteIntent.NavigateToLogin) }
                )
            }
            else -> PropertyList(
                list = state.properties,
                currencyRates = state.currencyRates,
                onFavoriteClick = { intent(FavoriteIntent.ToggleFavorite(it)) },
                onItemClick = { intent(FavoriteIntent.OnPropertyClick(it)) }
            )
        }
    }
}