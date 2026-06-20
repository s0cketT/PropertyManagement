package com.example.propertymanagement.ui.profile_screen.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.HorizontalPadding
import com.example.propertymanagement.ui.theme.IconSizeProfile
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.ProfileHeaderAvatarSize
import com.example.propertymanagement.ui.theme.ProfileSectionCardElevation
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SurfaceTonalElevationLow
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
                is ProfileEvent.NavigateToMyApplications -> {
                    navController.navigate(Screens.MyApplicationsScreen.route)
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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            state.user != null -> {
                AuthorizedContent(
                    state = state,
                    intent = intent,
                )
            }

            else -> {
                UnauthorizedContent(
                    onLogin = { intent(ProfileIntent.LoginClick) },
                    onSettings = { intent(ProfileIntent.Settings) },
                )
            }
        }
    }
}

@Composable
private fun UnauthorizedContent(
    onLogin: () -> Unit,
    onSettings: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = HorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(SpacerMedium))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = PaddingLarge,
                    vertical = PaddingLarge + PaddingLarge,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(
                    modifier = Modifier.size(ProfileHeaderAvatarSize),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = SurfaceTonalElevationLow,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(IconSizeProfile + 8.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(SpacerMedium))

                Text(
                    text = stringResource(R.string.profile_guest_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(PaddingLarge / 2))

                Text(
                    text = stringResource(R.string.profile_guest_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(SpacerMedium))

                Button(
                    onClick = onLogin,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Text(text = stringResource(R.string.login))
                }
            }
        }

        Spacer(modifier = Modifier.height(SpacerMedium))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
        ) {
            ProfileActionItem(
                textRes = R.string.settings_app,
                icon = Icons.Default.Settings,
                horizontalContentPadding = PaddingLarge,
                onClick = onSettings,
            )
        }

        Spacer(modifier = Modifier.height(SpacerMedium))
    }
}