package com.example.propertymanagement.ui.profile_screen.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.profile_screen.ProfileEvent
import com.example.propertymanagement.ui.profile_screen.ProfileIntent
import com.example.propertymanagement.ui.profile_screen.ProfileState
import com.example.propertymanagement.ui.profile_screen.ProfileViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController
) {

    val profileViewModel: ProfileViewModel = koinViewModel()
    val state by profileViewModel.state.collectAsStateWithLifecycle()
    val intent = profileViewModel::processIntent
    val event: Flow<ProfileEvent> by remember { mutableStateOf(profileViewModel.event) }
    val context = LocalContext.current
    val ratingSavedText = stringResource(R.string.rate_app_saved)
    val ratingFailedText = stringResource(R.string.rate_app_save_failed)

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is ProfileEvent.NavigateBack -> navController.popBackStack()
                is ProfileEvent.NavigateToAuth -> {
                    navController.navigate(Screens.AuthLoginScreen.route) {
                        popUpTo(0)
                    }
                }

                is ProfileEvent.NavigateToPublishScreen -> {
                    navController.navigate(Screens.Publish.route) {
                        popUpTo(0)
                    }
                }

                is ProfileEvent.NavigateToMyAds -> {
                    navController.navigate(Screens.MyAdsScreen.createRoute())
                }
                is ProfileEvent.NavigateToSettings -> { navController.navigate(Screens.SettingsScreen.route) }
                is ProfileEvent.NavigateToPersonalInfo -> {
                    navController.navigate(Screens.PersonalInfoScreen.createRoute(state.user!!))
                }

                ProfileEvent.AppRatingSaved -> {
                    Toast.makeText(context, ratingSavedText, Toast.LENGTH_SHORT).show()
                }

                ProfileEvent.AppRatingSaveFailed -> {
                    Toast.makeText(context, ratingFailedText, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BackHandler {
        if (state.isRateAppSheetOpen) {
            intent(ProfileIntent.DismissRateAppSheet)
        } else {
            intent(ProfileIntent.NavigateBack)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        UI(state = state, intent = intent)

        RateAppBottomSheet(
            visible = state.isRateAppSheetOpen,
            isSubmitting = state.isSavingAppRating,
            onDismiss = { intent(ProfileIntent.DismissRateAppSheet) },
            onSubmitRating = { stars -> intent(ProfileIntent.SubmitAppRating(stars)) }
        )
    }
}

@Composable
private fun UI(
    state: ProfileState,
    intent: (ProfileIntent) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.user != null -> {
                AuthorizedContent(
                    state = state,
                    intent = intent
                )
            }

            else -> {
                UnauthorizedContent(
                    onLogin = { intent(ProfileIntent.LoginClick) }
                )
            }
        }
    }
}

@Composable
private fun UnauthorizedContent(
    onLogin: () -> Unit
) {
    Button(onClick = onLogin) {
        Text(text = stringResource(R.string.login))
    }
}