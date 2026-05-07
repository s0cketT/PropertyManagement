package com.example.propertymanagement.ui.splash_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.bottom_nav.navigateToMainOrMyAdsFromModerationPush
import com.example.propertymanagement.ui.splash_screen.SplashEvent
import com.example.propertymanagement.ui.splash_screen.SplashViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navController: NavController,
) {

    val splashViewModel: SplashViewModel = koinViewModel()
    val event: Flow<SplashEvent> by remember { mutableStateOf(splashViewModel.event) }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                SplashEvent.NavigateToMain -> {
                    navController.navigateToMainOrMyAdsFromModerationPush()
                }

                SplashEvent.NavigateToAuth -> {
                    navController.navigate(Screens.AuthLoginScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    UI()
}

@Composable
private fun UI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
