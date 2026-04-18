package com.example.propertymanagement.ui.bottom_nav


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.ui.auth_screen.AuthCheck
import com.example.propertymanagement.ui.auth_screen.components.AuthLoginScreen
import com.example.propertymanagement.ui.auth_screen.components.AuthOtpScreen
import com.example.propertymanagement.ui.auth_screen.components.AuthRegisterScreen
import com.example.propertymanagement.ui.extensions.toUserProfile
import com.example.propertymanagement.ui.favorites_screen.conponents.FavoritesScreen
import com.example.propertymanagement.ui.filters_screen.components.CategorySelectionScreen
import com.example.propertymanagement.ui.filters_screen.components.FilterScreen
import com.example.propertymanagement.ui.list_property_screen.components.ListPropertyScreen
import com.example.propertymanagement.ui.map_screen.components.MapScreen
import com.example.propertymanagement.ui.my_ads_screen.components.MyAdsScreen
import com.example.propertymanagement.ui.personal_info_screen.components.PersonalInfoScreen
import com.example.propertymanagement.ui.profile_screen.components.ProfileScreen
import com.example.propertymanagement.ui.property_detail_screen.components.PropertyDetailScreen
import com.example.propertymanagement.ui.publish_screen.components.PublishScreen
import com.example.propertymanagement.ui.settings_screen.components.SettingsScreen
import com.example.propertymanagement.ui.splash_screen.components.SplashScreen

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
            //startDestination = Screens.Advertisements.route,
            startDestination = Screens.SplashScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screens.Advertisements.route) {
                ListPropertyScreen(navController = navController)
            }

            composable(Screens.Favorites.route) {
                FavoritesScreen(navController = navController)
            }

            composable(Screens.Map.route) {
                MapScreen(navController = navController)
            }

            composable(Screens.Publish.route) {
                PublishScreen(navController =navController)
            }

            composable(Screens.Profile.route) {
                ProfileScreen(navController = navController)
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

            composable(
                route = Screens.AuthRegister.route,
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) {
                AuthRegisterScreen(navController = navController)
            }

            composable(
                route = Screens.AuthOtpScreen.route,
                arguments = listOf(
                    navArgument("email") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("check") {
                        type = NavType.StringType
                        defaultValue = AuthCheck.LOGIN.name
                    }
                ),
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) { backStackEntry ->

                val email = backStackEntry.arguments?.getString("email").orEmpty()

                val check = backStackEntry.arguments
                    ?.getString("check")
                    ?.let { AuthCheck.valueOf(it) }
                    ?: AuthCheck.LOGIN

                AuthOtpScreen(
                    navController = navController,
                    email = email,
                    check = check
                )
            }

            composable(
                route = Screens.AuthLoginScreen.route,
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) {
                AuthLoginScreen(navController = navController)
            }

            composable(Screens.SplashScreen.route) {
                SplashScreen(navController = navController)
            }

            composable(
                route = Screens.SettingsScreen.route,
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) {
                SettingsScreen(navController = navController)
            }

            composable(
                route = Screens.MyAdsScreen.route,
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) {
                MyAdsScreen(navController = navController)
            }

            composable(
                route = Screens.PersonalInfoScreen.route,
                arguments = listOf(
                    navArgument("user") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                ),
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) { backStackEntry ->

                val userJson = backStackEntry.arguments?.getString("user").orEmpty()

                val user: UserProfile = userJson.toUserProfile()

                PersonalInfoScreen(
                    navController = navController,
                    user = user
                )
            }

            composable(
                route = Screens.PropertyDetailScreen.route,
                arguments = listOf(
                    navArgument("propertyId") {
                        type = NavType.IntType
                    },
                    navArgument("userId") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                ),
                enterTransition = AppTransitions.slideFromRight.enter,
                exitTransition = AppTransitions.slideFromRight.exit,
                popEnterTransition = AppTransitions.slideFromRight.popEnter,
                popExitTransition = AppTransitions.slideFromRight.popExit
            ) { backStackEntry ->

                val propertyId = backStackEntry.arguments?.getInt("propertyId") ?: 0
                val userId = backStackEntry.arguments?.getString("userId").orEmpty()

                PropertyDetailScreen(
                    navController = navController,
                    propertyId = propertyId,
                    userId = userId
                )
            }
        }
    }
}