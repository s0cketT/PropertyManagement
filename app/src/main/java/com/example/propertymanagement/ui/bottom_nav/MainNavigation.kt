package com.example.propertymanagement.ui.bottom_nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.propertymanagement.ui.filters_screen.components.FilterScreen
import com.example.propertymanagement.ui.favorites_screen.FavoritesScreen
import com.example.propertymanagement.ui.filters_screen.components.CategorySelectionScreen
import com.example.propertymanagement.ui.list_property_screen.ListPropertyScreen
import com.example.propertymanagement.ui.map_screen.components.MapScreen
import com.example.propertymanagement.ui.profile_screen.ProfileScreen
import com.example.propertymanagement.ui.publish_screen.PublishScreen

@Composable
fun MainNavigation() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Screens.Advertisements.route,
        Screens.Favorites.route,
        Screens.Publish.route,
        Screens.Profile.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screens.Advertisements.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screens.Advertisements.route) {
                ListPropertyScreen()
            }

            composable(Screens.Favorites.route) {
                FavoritesScreen()
            }

            composable(Screens.Map.route) {
                MapScreen(navController = navController)
            }

            composable(Screens.Publish.route) {
                PublishScreen()
            }

            composable(Screens.Profile.route) {
                ProfileScreen()
            }

            composable(
                route = Screens.Filters.route,
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) {
                FilterScreen(navController = navController)
            }

            composable(
                route = Screens.CategorySelection.route,
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) {
                CategorySelectionScreen(navController = navController)
            }
        }
    }
}