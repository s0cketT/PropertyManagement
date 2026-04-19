package com.example.propertymanagement.ui.auth_screen.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.auth_screen.AuthCheck
import com.example.propertymanagement.ui.auth_screen.AuthError
import com.example.propertymanagement.ui.auth_screen.AuthEvent
import com.example.propertymanagement.ui.auth_screen.AuthIntent
import com.example.propertymanagement.ui.auth_screen.AuthState
import com.example.propertymanagement.ui.auth_screen.AuthViewModel
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.HeightFilterChip
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerLarge
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.TextFieldBorderWidth
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthOtpScreen(
    navController: NavController,
    email: String,
    check: AuthCheck
) {
    val authViewModel: AuthViewModel = koinViewModel()
    val state by authViewModel.state.collectAsStateWithLifecycle()
    val intent = authViewModel::processIntent
    val event: Flow<AuthEvent> by remember { mutableStateOf(authViewModel.event) }

    val context = LocalContext.current
    val successMessage = stringResource(R.string.registration_success)

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is AuthEvent.NavigateToLoginScreen -> { navController.navigate(Screens.AuthLoginScreen.route) }
                is AuthEvent.ShowRegistrationSuccess -> { Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show() }
                is AuthEvent.NavigateToMain -> {
                    navController.navigate(Screens.Advertisements.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                AuthEvent.NavigateToSetNewPassword -> {
                    navController.navigate(Screens.SetNewPasswordScreen.route)
                }
                AuthEvent.NavigateToChangeNewEmail -> {
                    navController.navigate(Screens.ChangeNewEmailScreen.route)
                }
                AuthEvent.NavigateBack -> { navController.popBackStack() }
                else -> Unit
            }
        }
    }

    BackHandler {
        intent(AuthIntent.NavigateBack)
    }

    LaunchedEffect(Unit) {
        intent(AuthIntent.EmailChanged(email = email))
        intent(AuthIntent.AuthCheckChanged(check = check))
    }

    UI(
        state = state,
        check = check,
        intent = intent
    )

}


@Composable
private fun UI(
    state: AuthState,
    check: AuthCheck,
    intent: (AuthIntent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val titleRes = when (check) {
            AuthCheck.CHANGE_EMAIL_CONFIRM_OLD -> R.string.change_email_otp_old_title
            AuthCheck.RESET_PASSWORD -> R.string.change_password_otp_title
            else -> R.string.enter_code_from_email
        }
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        when (check) {
            AuthCheck.CHANGE_EMAIL_CONFIRM_OLD -> {
                Spacer(modifier = Modifier.height(PaddingLarge))
                Text(
                    text = stringResource(R.string.change_email_otp_old_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AuthCheck.RESET_PASSWORD -> {
                Spacer(modifier = Modifier.height(PaddingLarge))
                Text(
                    text = stringResource(R.string.change_password_otp_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            else -> Unit
        }

        Spacer(modifier = Modifier.height(SpacerLarge))

        OtpInput(
            code = state.code,
            onCodeChange = { intent(AuthIntent.CodeChanged(it)) }
        )

        Spacer(modifier = Modifier.height(SpacerLarge))

        state.otpError?.let { error ->
            Spacer(modifier = Modifier.height(SpacerSmall))
            Text(
                text = when (error) {
                    AuthError.InvalidOtp -> stringResource(R.string.invalid_otp)
                    else -> stringResource(R.string.error_unknown)
                },
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(SpacerLarge))

        Button(
            onClick = { intent(AuthIntent.VerifyOtp) },
            enabled = state.code.length == 6 && !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.confirm))
        }
    }

    LaunchedEffect(state.code) {
        if (state.code.length == 6 && !state.isLoading) {
            intent(AuthIntent.VerifyOtp)
        }
    }
}

@Composable
fun OtpInput(
    code: String,
    onCodeChange: (String) -> Unit
) {

    Box {
        BasicTextField(
            value = code,
            onValueChange = {
                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                    onCodeChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(6) { index ->
                OtpCell(
                    char = code.getOrNull(index)?.toString() ?: "",
                    isActive = index == code.length
                )
            }
        }
    }
}

@Composable
fun OtpCell(
    char: String,
    isActive: Boolean,
) {
    val borderColor = if (isActive) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(HeightFilterChip)
            .border(
                width = TextFieldBorderWidth,
                color = borderColor,
                shape = RoundedCornerShape(ButtonCornerRadius)
            )
    ) {
        Text(
            text = char,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}