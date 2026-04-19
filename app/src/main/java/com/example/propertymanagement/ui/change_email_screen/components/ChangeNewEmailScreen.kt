package com.example.propertymanagement.ui.change_email_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.auth_screen.components.AuthTextField
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.change_email_screen.ChangeEmailLinkIntent
import com.example.propertymanagement.ui.change_email_screen.ChangeNewEmailEvent
import com.example.propertymanagement.ui.change_email_screen.ChangeNewEmailIntent
import com.example.propertymanagement.ui.change_email_screen.ChangeNewEmailState
import com.example.propertymanagement.ui.change_email_screen.ChangeNewEmailViewModel
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.components.PrimaryActionButton
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChangeNewEmailScreen(navController: NavController) {
    val viewModel: ChangeNewEmailViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val intent = viewModel::processIntent
    val event: Flow<ChangeNewEmailEvent> by remember { mutableStateOf(viewModel.event) }

    LaunchedEffect(Unit) {
        event.collect { ev ->
            when (ev) {
                is ChangeNewEmailEvent.NavigateToEmailLinkInstructions -> {
                    navController.navigate(Screens.ChangeEmailLinkScreen.createRoute(ev.email))
                }

                is ChangeNewEmailEvent.NavigateBackToSettings -> { navController.navigate(Screens.SettingsScreen.route) }
            }
        }
    }

    BackHandler {
        intent(ChangeNewEmailIntent.NavigateBack)
    }

    UI(
        state = state,
        intent = intent
    )
}

@Composable
private fun UI(
    state: ChangeNewEmailState,
    intent: (ChangeNewEmailIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingLarge)
    ) {
        AppTopBar(
            title = R.string.change_email_new_title,
            onBackClick = { intent(ChangeNewEmailIntent.NavigateBack) }
        )

        Spacer(modifier = Modifier.height(SpacerMedium))

        Text(
            text = stringResource(R.string.change_email_new_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(SpacerMedium))

        AuthTextField(
            value = state.newEmail,
            placeholderRes = R.string.email,
            error = state.emailError,
            onValueChange = { intent(ChangeNewEmailIntent.NewEmailChanged(it)) }
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryActionButton(
            text = R.string.change_email_send_code_to_new,
            onClick = { intent(ChangeNewEmailIntent.Submit) },
            enabled = !state.isLoading
        )

        Spacer(modifier = Modifier.height(PaddingLarge))
    }
}
