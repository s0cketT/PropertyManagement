package com.example.propertymanagement.ui.bottom_nav


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.propertymanagement.domain.model.MyAdsListingFilter
import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.ui.auth_screen.AuthCheck
import com.example.propertymanagement.ui.auth_screen.components.AuthLoginScreen
import com.example.propertymanagement.ui.auth_screen.components.AuthOtpScreen
import com.example.propertymanagement.ui.auth_screen.components.AuthRegisterScreen
import com.example.propertymanagement.ui.change_email_screen.components.ChangeEmailLinkInstructionScreen
import com.example.propertymanagement.ui.change_email_screen.components.ChangeNewEmailScreen
import com.example.propertymanagement.ui.change_password_screen.components.SetNewPasswordScreen
import com.example.propertymanagement.ui.extensions.toUserProfile
import com.example.propertymanagement.ui.favorites_screen.conponents.FavoritesScreen
import com.example.propertymanagement.ui.filters_screen.components.CategorySelectionScreen
import com.example.propertymanagement.ui.filters_screen.components.CitySelectionScreen
import com.example.propertymanagement.ui.filters_screen.components.FilterScreen
import com.example.propertymanagement.ui.filters_screen.components.RegionSelectionScreen
import com.example.propertymanagement.ui.list_property_screen.components.ListPropertyScreen
import com.example.propertymanagement.ui.map_screen.components.MapScreen
import com.example.propertymanagement.ui.my_ads_screen.components.MyAdsScreen
import com.example.propertymanagement.ui.my_applications_screen.components.MyApplicationsScreen
import com.example.propertymanagement.ui.personal_info_screen.components.PersonalInfoScreen
import com.example.propertymanagement.ui.profile_screen.components.ProfileScreen
import com.example.propertymanagement.ui.property_detail_screen.components.PropertyDetailScreen
import com.example.propertymanagement.ui.edit_property_screen.components.EditPropertyScreen
import com.example.propertymanagement.ui.publish_screen.components.PublishScreen
import com.example.propertymanagement.ui.settings_screen.components.SettingsScreen
import com.example.propertymanagement.ui.splash_screen.components.SplashScreen
import com.example.propertymanagement.data.common.PushNotificationNavigation

@Composable
fun MainNavigation() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val pendingMyAdsTab by PushNotificationNavigation.pendingMyAdsTab.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = currentRoute, key2 = pendingMyAdsTab) {
        if (pendingMyAdsTab == null) {
            return@LaunchedEffect
        }
        val route = currentRoute ?: return@LaunchedEffect
        if (route == Screens.SplashScreen.route || isAuthRoute(route)) {
            return@LaunchedEffect
        }

        val tab = PushNotificationNavigation.takePendingMyAdsTab() ?: return@LaunchedEffect
        navController.resetStackToAdvertisementsThenMyAds(tab)
    }

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
            modifier = Modifier.padding(innerPadding),
            enterTransition = AppTransitions.instant.enter,
            exitTransition = AppTransitions.instant.exit,
            popEnterTransition = AppTransitions.instant.popEnter,
            popExitTransition = AppTransitions.instant.popExit,
            sizeTransform = { null },
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
                PublishScreen(navController = navController)
            }

            composable(
                route = Screens.EditPropertyScreen.route,
                arguments = listOf(
                    navArgument("propertyId") {
                        type = NavType.IntType
                    },
                ),
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit,
            ) { backStackEntry ->
                val propertyId = backStackEntry.arguments?.getInt("propertyId")
                if (propertyId != null) {
                    EditPropertyScreen(
                        navController = navController,
                        propertyId = propertyId,
                    )
                }
            }

            composable(Screens.Profile.route) {
                ProfileScreen(navController = navController)
            }

            composable(
                route = Screens.Filters.route,
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) {
                FilterScreen(navController = navController)
            }

            composable(
                route = Screens.CategorySelection.route,
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) {
                CategorySelectionScreen(navController = navController)
            }

            composable(
                route = Screens.RegionSelection.route,
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) {
                RegionSelectionScreen(navController = navController)
            }

            composable(
                route = Screens.CitySelection.route,
                arguments = listOf(
                    navArgument("regionId") {
                        type = NavType.LongType
                    },
                    navArgument("regionName") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                ),
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) { backStackEntry ->
                val regionId = backStackEntry.arguments?.getLong("regionId") ?: 0L
                val regionName = backStackEntry.arguments?.getString("regionName").orEmpty()

                CitySelectionScreen(
                    navController = navController,
                    regionId = regionId,
                    regionName = regionName,
                )
            }

            composable(
                route = Screens.AuthRegister.route,
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
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
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
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
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) {
                AuthLoginScreen(navController = navController)
            }

            composable(
                route = Screens.SetNewPasswordScreen.route,
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) {
                SetNewPasswordScreen(navController = navController)
            }

            composable(
                route = Screens.ChangeNewEmailScreen.route,
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) {
                ChangeNewEmailScreen(navController = navController)
            }

            composable(
                route = Screens.ChangeEmailLinkScreen.route,
                arguments = listOf(
                    navArgument("email") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                ),
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) { entry ->
                val email = entry.arguments?.getString("email").orEmpty()
                ChangeEmailLinkInstructionScreen(
                    navController = navController,
                    newEmail = email
                )
            }

            composable(Screens.SplashScreen.route) {
                SplashScreen(navController = navController)
            }

            composable(
                route = Screens.SettingsScreen.route,
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) {
                SettingsScreen(navController = navController)
            }

            composable(
                route = Screens.MyAdsScreen.route,
                arguments = listOf(
                    navArgument("initialTab") {
                        type = NavType.StringType
                        defaultValue = MyAdsListingFilter.PUBLISHED.name
                    },
                ),
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
            ) { backStackEntry ->
                val tabName = backStackEntry.arguments?.getString("initialTab")
                val initialTab = try {
                    tabName?.let { MyAdsListingFilter.valueOf(it) }
                } catch (_: IllegalArgumentException) {
                    null
                } ?: MyAdsListingFilter.PUBLISHED

                MyAdsScreen(
                    navController = navController,
                    initialListingFilter = initialTab,
                )
            }

            composable(
                route = Screens.MyApplicationsScreen.route,
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit,
            ) {
                MyApplicationsScreen(navController = navController)
            }

            composable(
                route = Screens.PersonalInfoScreen.route,
                arguments = listOf(
                    navArgument("user") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                ),
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
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
                enterTransition = AppTransitions.instant.enter,
                exitTransition = AppTransitions.instant.exit,
                popEnterTransition = AppTransitions.instant.popEnter,
                popExitTransition = AppTransitions.instant.popExit
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

private fun isAuthRoute(route: String): Boolean {
    return route == Screens.AuthLoginScreen.route ||
        route == Screens.AuthRegister.route ||
        route.startsWith("auth_otp") ||
        route == Screens.SetNewPasswordScreen.route ||
        route == Screens.ChangeNewEmailScreen.route ||
        route.startsWith("change_email_link")
}