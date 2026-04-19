package com.example.propertymanagement.ui.change_email_screen.components

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.change_email_screen.ChangeEmailLinkEvent
import com.example.propertymanagement.ui.change_email_screen.ChangeEmailLinkIntent
import com.example.propertymanagement.ui.change_email_screen.ChangeEmailLinkState
import com.example.propertymanagement.ui.change_email_screen.ChangeEmailLinkViewModel
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.components.PrimaryActionButton
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChangeEmailLinkInstructionScreen(
    navController: NavController,
    newEmail: String
) {
    val viewModel: ChangeEmailLinkViewModel = koinViewModel(
        parameters = { parametersOf(newEmail) }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val intent = viewModel::processIntent
    val event: Flow<ChangeEmailLinkEvent> by remember { mutableStateOf(viewModel.event) }
    val context = LocalContext.current
    val successMsg = stringResource(R.string.change_email_success)

    LaunchedEffect(Unit) {
        event.collect { ev ->
            when (ev) {
                is ChangeEmailLinkEvent.Success -> {
                    Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
                    navController.popBackStack(Screens.SettingsScreen.route, inclusive = false)
                }
                is ChangeEmailLinkEvent.NavigateBack -> { navController.popBackStack() }
            }
        }
    }

    BackHandler {
        intent(ChangeEmailLinkIntent.NavigateBack)
    }

    UI(
        newEmail = newEmail,
        state = state,
        intent = intent
    )
}

@Composable
private fun UI(
    newEmail: String,
    state: ChangeEmailLinkState,
    intent: (ChangeEmailLinkIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingLarge)
    ) {
        AppTopBar(
            title = R.string.change_email_link_title,
            onBackClick = { intent(ChangeEmailLinkIntent.NavigateBack) }
        )

        Spacer(modifier = Modifier.height(SpacerMedium))

        Text(
            text = stringResource(R.string.change_email_link_body, newEmail),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (state.showNotConfirmedError) {
            Spacer(modifier = Modifier.height(SpacerMedium))
            Text(
                text = stringResource(R.string.change_email_link_not_confirmed),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryActionButton(
            text = R.string.change_email_link_check_button,
            onClick = { intent(ChangeEmailLinkIntent.CheckSynced) },
            enabled = !state.isLoading
        )

        Spacer(modifier = Modifier.height(PaddingLarge))
    }
}
