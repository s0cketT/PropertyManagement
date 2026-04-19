package com.example.propertymanagement.ui.change_password_screen.components

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
import com.example.propertymanagement.ui.auth_screen.components.AuthTextField
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.change_password_screen.SetNewPasswordEvent
import com.example.propertymanagement.ui.change_password_screen.SetNewPasswordIntent
import com.example.propertymanagement.ui.change_password_screen.SetNewPasswordState
import com.example.propertymanagement.ui.change_password_screen.SetNewPasswordViewModel
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.components.PrimaryActionButton
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetNewPasswordScreen(navController: NavController) {
    val viewModel: SetNewPasswordViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val intent = viewModel::processIntent
    val event: Flow<SetNewPasswordEvent> by remember { mutableStateOf(viewModel.event) }
    val context = LocalContext.current
    val successMsg = stringResource(R.string.change_password_success)

    LaunchedEffect(Unit) {
        event.collect { ev ->
            when (ev) {
                SetNewPasswordEvent.Success -> {
                    Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
                }
                SetNewPasswordEvent.NavigateBackToSettings -> {
                    navController.popBackStack(Screens.SettingsScreen.route, inclusive = false)
                }
            }
        }
    }

    BackHandler {
        intent(SetNewPasswordIntent.NavigateBack)
    }

    UI(
        state = state,
        intent = intent
    )
}

@Composable
private fun UI(
    state: SetNewPasswordState,
    intent: (SetNewPasswordIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingLarge)
    ) {
        AppTopBar(
            title = R.string.change_password_new_password_title,
            onBackClick = { intent(SetNewPasswordIntent.NavigateBack) }
        )

        Spacer(modifier = Modifier.height(SpacerMedium))

        Text(
            text = stringResource(R.string.change_password_new_password_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(SpacerMedium))

        AuthTextField(
            value = state.password,
            placeholderRes = R.string.password,
            error = state.passwordError,
            isPassword = true,
            onValueChange = { intent(SetNewPasswordIntent.PasswordChanged(it)) }
        )

        Spacer(modifier = Modifier.height(PaddingLarge))

        AuthTextField(
            value = state.confirmPassword,
            placeholderRes = R.string.confirm_password,
            error = state.confirmPasswordError,
            isPassword = true,
            onValueChange = { intent(SetNewPasswordIntent.ConfirmPasswordChanged(it)) }
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryActionButton(
            text = R.string.save,
            onClick = { intent(SetNewPasswordIntent.Submit) },
            enabled = !state.isLoading
        )

        Spacer(modifier = Modifier.height(PaddingLarge))
    }
}
