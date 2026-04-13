package com.example.propertymanagement.ui.auth_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.auth_screen.AuthCheck
import com.example.propertymanagement.ui.auth_screen.AuthEvent
import com.example.propertymanagement.ui.auth_screen.AuthIntent
import com.example.propertymanagement.ui.auth_screen.AuthState
import com.example.propertymanagement.ui.auth_screen.AuthViewModel
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.TextLarge
import com.example.propertymanagement.ui.theme.TextRegular
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthLoginScreen(navController: NavController) {

    val authViewModel: AuthViewModel = koinViewModel()
    val state by authViewModel.state.collectAsStateWithLifecycle()
    val intent = authViewModel::processIntent
    val event: Flow<AuthEvent> by remember { mutableStateOf(authViewModel.event) }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is AuthEvent.NavigateToRegisterScreen -> {
                    navController.navigate(Screens.AuthRegister.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                is AuthEvent.NavigateToMain -> {
                    navController.navigate(Screens.Advertisements.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                is AuthEvent.NavigateToCodeScreen -> {
                    navController.navigate(Screens.AuthOtpScreen.createRoute(event.email,  AuthCheck.LOGIN)) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                is AuthEvent.NavigateAsGuest -> {
                    navController.navigate(Screens.Advertisements.route) {
                        popUpTo(0) { inclusive = true }
                } }
                else -> Unit
            }
        }
    }

    UI(
        state = state,
        intent = intent
    )

}

@Composable
private fun UI(
    state: AuthState,
    intent: (AuthIntent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingLarge),
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
            ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = TextLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(PaddingLarge))

        AuthTextField(
            value = state.email,
            placeholderRes = R.string.email,
            error = state.emailError,
            onValueChange = { intent(AuthIntent.EmailChanged(it)) }
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        if (!state.isOtpLogin) {
            AuthTextField(
                value = state.password,
                placeholderRes = R.string.password,
                error = state.passwordError,
                isPassword = true,
                onValueChange = { intent(AuthIntent.PasswordChanged(it)) }
            )

            Spacer(modifier = Modifier.height(PaddingSmall))

            Text(
                text = stringResource(R.string.forgot_password),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = TextRegular,
                modifier = Modifier.clickable {
                    intent(AuthIntent.ToggleLoginMode)
                }
            )
        } else {
            Spacer(modifier = Modifier.height(PaddingSmall))

            Text(
                text = stringResource(R.string.login_with_password),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = TextRegular,
                modifier = Modifier.clickable {
                    intent(AuthIntent.ToggleLoginMode)
                }
            )
        }

        Spacer(modifier = Modifier.height(PaddingLarge))

        Button(
            onClick = {
                if (state.isOtpLogin) {
                    intent(AuthIntent.SendOtp)
                } else {
                    intent(AuthIntent.Login)
                }
            },
            enabled = !state.isLoading && state.email.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (state.isOtpLogin)
                    stringResource(R.string.get_code)
                else
                    stringResource(R.string.login)
            )
        }

        Spacer(modifier = Modifier.height(PaddingMedium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(PaddingSmall))

            Text(
                text = stringResource(R.string.register),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    intent(AuthIntent.NavigateToRegister)
                }
            )
        }

        Spacer(modifier = Modifier.height(PaddingMedium))

        Text(
            text = stringResource(R.string.continue_as_guest),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = TextRegular,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    intent(AuthIntent.ContinueAsGuest)
                }
        )

    }
}