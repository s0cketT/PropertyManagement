package com.example.propertymanagement.ui.auth_screen.components

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.ui.auth_screen.AuthCheck
import com.example.propertymanagement.ui.auth_screen.AuthError
import com.example.propertymanagement.ui.auth_screen.AuthEvent
import com.example.propertymanagement.ui.auth_screen.AuthIntent
import com.example.propertymanagement.ui.auth_screen.AuthState
import com.example.propertymanagement.ui.auth_screen.AuthViewModel
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.mapper.asString
import com.example.propertymanagement.ui.publish_screen.components.PropertyTitleTextField
import com.example.propertymanagement.ui.theme.IconSizeArrow
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.TextLarge
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthRegisterScreen(navController: NavController) {

    val authViewModel: AuthViewModel = koinViewModel()
    val state by authViewModel.state.collectAsStateWithLifecycle()
    val intent = authViewModel::processIntent
    val event: Flow<AuthEvent> by remember { mutableStateOf(authViewModel.event) }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is AuthEvent.NavigateToCodeScreen -> {
                    navController.navigate(
                        Screens.AuthOtpScreen.createRoute(event.email,  AuthCheck.REGISTER)
                    )
                }

                AuthEvent.NavigateToLoginScreen -> {
                    navController.navigate(Screens.AuthLoginScreen.route)
                }

                else -> Unit
            }
        }
    }

    UI(
        state = state,
        onIntent = intent,
    )

    PrivacyPolicyBottomSheet(
        visible = state.showPrivacyPolicySheet,
        onDismiss = { intent(AuthIntent.DismissPrivacyPolicy) },
    )
}

@Composable
private fun UI(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(PaddingLarge),
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.register),
                fontSize = TextLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(PaddingLarge))

        AuthTextField(
            value = state.firstName,
            placeholderRes = R.string.first_name,
            error = state.firstNameError,
            onValueChange = { onIntent(AuthIntent.FirstNameChanged(it)) }
        )

        Spacer(modifier = Modifier.height(PaddingLarge))

        Text(
            text = stringResource(R.string.phone),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = PaddingSmall),
        )

        AuthBelarusPhoneTextField(
            nationalDigits = state.phoneNationalDigits,
            error = state.phoneError,
            onNationalDigitsChange = { onIntent(AuthIntent.PhoneNationalDigitsChanged(it)) },
        )

        Spacer(modifier = Modifier.height(PaddingLarge))

        AuthTextField(
            value = state.email,
            placeholderRes = R.string.email,
            error = state.emailError,
            info = state.emailInfo,
            onValueChange = { onIntent(AuthIntent.EmailChanged(it)) }
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        SellerTypeDropdown(
            selected = state.sellerType,
            error = state.sellerTypeError,
            onSelect = { onIntent(AuthIntent.SellerTypeChanged(it)) }
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        AuthTextField(
            value = state.password,
            placeholderRes = R.string.password,
            error = state.passwordError,
            isPassword = true,
            onValueChange = { onIntent(AuthIntent.PasswordChanged(it)) }
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        AuthTextField(
            value = state.confirmPassword,
            placeholderRes = R.string.confirm_password,
            error = state.confirmPasswordError,
            isPassword = true,
            onValueChange = { onIntent(AuthIntent.ConfirmPasswordChanged(it)) }
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        PrivacyPolicyConsentRow(
            isAccepted = state.privacyPolicyAccepted,
            error = state.privacyPolicyError,
            onAcceptedChange = { onIntent(AuthIntent.PrivacyPolicyAcceptedChanged(it)) },
            onOpenPolicy = { onIntent(AuthIntent.OpenPrivacyPolicy) },
        )

        Spacer(modifier = Modifier.height(PaddingLarge))

        Button(
            onClick = { onIntent(AuthIntent.Submit) },
            enabled = !state.isLoading &&
                    state.privacyPolicyAccepted &&
                    state.emailError == null &&
                    state.passwordError == null &&
                    state.phoneError == null &&
                    state.confirmPasswordError == null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.register)
            )
        }

        Spacer(modifier = Modifier.height(PaddingMedium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(PaddingSmall))

            Text(
                text = stringResource(R.string.login),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    onIntent(AuthIntent.NavigateToLogin)
                }
            )
        }
    }
}

@Composable
private fun SellerTypeDropdown(
    selected: SellerType?,
    error: AuthError?,
    onSelect: (SellerType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            PropertyTitleTextField(
                value = selected?.asString() ?: "",
                placeholder = stringResource(R.string.select_seller_type),
                isError = error != null, // ✅ вот это важно
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = true }
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = PaddingMedium)
                    .size(IconSizeArrow)
            )
        }

        error?.let {
            Text(
                text = it.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = PaddingSmall, top = PaddingSmall)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            SellerType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.asString()) },
                    onClick = {
                        onSelect(type)
                        expanded = false
                    }
                )
            }
        }
    }
}