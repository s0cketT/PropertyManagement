package com.example.propertymanagement.ui.settings_screen.components

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.ThemeType
import com.example.propertymanagement.ui.auth_screen.AuthCheck
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.components.EnumRadioSection
import com.example.propertymanagement.ui.components.PrimaryActionButton
import com.example.propertymanagement.ui.core.locale.LocaleManager
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.profile_screen.components.ProfileActionItem
import com.example.propertymanagement.ui.settings_screen.SettingsEvent
import com.example.propertymanagement.ui.settings_screen.SettingsIntent
import com.example.propertymanagement.ui.settings_screen.SettingsState
import com.example.propertymanagement.ui.settings_screen.SettingsViewModel
import com.example.propertymanagement.ui.theme.CardElevationLow
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.ProfileSectionCardElevation
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.SurfaceTonalElevationLow
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(navController: NavController) {

    val context = LocalContext.current
    val activity = context as Activity
    val changePasswordError = stringResource(R.string.change_password_send_code_error)
    val changeEmailError = stringResource(R.string.change_email_send_code_error)

    val settingsViewModel: SettingsViewModel = koinViewModel()
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    val intent = settingsViewModel::processIntent
    val event: Flow<SettingsEvent> by remember { mutableStateOf(settingsViewModel.event) }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is SettingsEvent.NavigateBack -> {
                    navController.navigate(Screens.Profile.route)
                }

                is SettingsEvent.ChangeLanguage -> {
                    LocaleManager.setLocale(state.selectedLanguage)
                    activity.recreate()
                }

                is SettingsEvent.NavigateToAuth -> {
                    navController.navigate(Screens.AuthLoginScreen.route) {
                        popUpTo(0)
                    }
                }

                is SettingsEvent.ApplyTheme -> {
                    activity.recreate()
                }

                is SettingsEvent.NavigateToPasswordResetOtp -> {
                    navController.navigate(
                        Screens.AuthOtpScreen.createRoute(event.email, AuthCheck.RESET_PASSWORD),
                    )
                }

                is SettingsEvent.ChangePasswordSendFailed -> {
                    Toast.makeText(context, changePasswordError, Toast.LENGTH_SHORT).show()
                }

                is SettingsEvent.NavigateToChangeEmailOtpOld -> {
                    navController.navigate(
                        Screens.AuthOtpScreen.createRoute(
                            event.email,
                            AuthCheck.CHANGE_EMAIL_CONFIRM_OLD,
                        ),
                    )
                }

                is SettingsEvent.ChangeEmailSendFailed -> {
                    Toast.makeText(context, changeEmailError, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BackHandler {
        intent(SettingsIntent.NavigateBack)
    }

    UI(
        state = state,
        intent = intent,
    )

    if (state.showChangePasswordSheet) {
        ChangePasswordInfoBottomSheet(
            isSending = state.isSendingChangePasswordCode,
            onDismiss = { intent(SettingsIntent.DismissChangePasswordSheet) },
            onContinue = { intent(SettingsIntent.ConfirmChangePasswordSendCode) },
        )
    }

    if (state.showChangeEmailSheet) {
        ChangeEmailInfoBottomSheet(
            isSending = state.isSendingChangeEmailCode,
            onDismiss = { intent(SettingsIntent.DismissChangeEmailSheet) },
            onContinue = { intent(SettingsIntent.ConfirmChangeEmailSendOldOtp) },
        )
    }
}

@Composable
private fun UI(
    state: SettingsState,
    intent: (SettingsIntent) -> Unit,
) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        AppTopBar(
            title = R.string.settings_app,
            onBackClick = { intent(SettingsIntent.NavigateBack) },
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scroll),
        ) {
            Spacer(modifier = Modifier.height(SpacerMedium))

            SettingsAppearanceCard(state = state, intent = intent)

            if (state.isAccountSectionVisible) {
                Spacer(modifier = Modifier.height(SpacerMedium))

                SettingsAccountCard(intent = intent)
            }

            Spacer(modifier = Modifier.height(SpacerMedium))
        }

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = SurfaceTonalElevationLow,
        ) {
            PrimaryActionButton(
                text = R.string.apply_text,
                onClick = { intent(SettingsIntent.Save) },
            )
        }
    }
}

@Composable
private fun SettingsAppearanceCard(
    state: SettingsState,
    intent: (SettingsIntent) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
    ) {
        Column(
            modifier = Modifier.padding(PaddingLarge),
        ) {

            Text(
                text = stringResource(R.string.language),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(SpacerSmall))

            LanguageSection(
                selectedLanguage = state.selectedLanguage,
                isVisible = state.showLanguageSheet,
                onClick = { intent(SettingsIntent.ToggleLanguageSheet(true)) },
                onSelect = { intent(SettingsIntent.SelectLanguage(it)) },
                onDismiss = { intent(SettingsIntent.ToggleLanguageSheet(false)) },
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = SpacerSmall),
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = CardElevationLow,
            )

            Text(
                text = stringResource(R.string.app_theme),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = SpacerSmall),
            )

            EnumRadioSection(
                entries = ThemeType.entries.toTypedArray(),
                selected = state.selectedTheme,
                onSelected = { intent(SettingsIntent.ChangeTheme(it)) },
                titleRes = { it.titleRes() },
            )
        }
    }
}

@Composable
private fun SettingsAccountCard(
    intent: (SettingsIntent) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
    ) {
        Column(
            modifier = Modifier.padding(
                bottom = PaddingLarge / 2,
            ),
        ) {
            Text(
                text = stringResource(R.string.settings_account_section_title),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    start = PaddingLarge,
                    end = PaddingLarge,
                    top = PaddingLarge,
                    bottom = SpacerSmall,
                ),
            )

            ProfileActionItem(
                textRes = R.string.settings_change_password,
                icon = Icons.Default.Lock,
                horizontalContentPadding = PaddingLarge,
                onClick = { intent(SettingsIntent.ChangePassword) },
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = PaddingLarge),
                thickness = CardElevationLow,
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            ProfileActionItem(
                textRes = R.string.settings_change_email,
                icon = Icons.Default.Email,
                horizontalContentPadding = PaddingLarge,
                onClick = { intent(SettingsIntent.ChangeEmail) },
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = PaddingLarge),
                thickness = CardElevationLow,
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            ProfileActionItem(
                textRes = R.string.logout,
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                iconTint = MaterialTheme.colorScheme.error,
                textTint = MaterialTheme.colorScheme.error,
                horizontalContentPadding = PaddingLarge,
                onClick = { intent(SettingsIntent.Logout) },
            )
        }
    }
}
