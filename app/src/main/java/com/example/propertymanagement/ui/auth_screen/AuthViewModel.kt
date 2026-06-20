package com.example.propertymanagement.ui.auth_screen

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.data.common.OneSignalManager
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.use_case.CheckUserExistsUseCase
import com.example.propertymanagement.domain.use_case.LogoutUseCase
import com.example.propertymanagement.domain.use_case.SendOtpUseCase
import com.example.propertymanagement.domain.use_case.SignInUseCase
import com.example.propertymanagement.domain.use_case.SignUpUseCase
import com.example.propertymanagement.domain.use_case.VerifyOtpUseCase
import com.example.propertymanagement.ui.auth_screen.components.belarusPhoneToE164
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val checkUserExistsUseCase: CheckUserExistsUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<AuthEvent>(viewModelScope)
    val event = _event.flow


    fun processIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.CodeChanged -> {
                _state.update {
                    it.copy(
                        code = intent.value,
                        otpError = null
                    )
                }
            }

            is AuthIntent.VerifyOtp -> {
                val current = _state.value
                onOtpEntered(email = current.email, code = current.code, check = current.check)
            }
            is AuthIntent.FirstNameChanged -> {
                _state.update {
                    it.copy(
                        firstName = intent.value,
                        firstNameError = null
                    )
                }
            }

            is AuthIntent.PhoneNationalDigitsChanged -> {
                _state.update {
                    it.copy(
                        phoneNationalDigits = intent.nationalDigits,
                        phoneError = null,
                    )
                }
            }

            is AuthIntent.EmailChanged -> {
                _state.update {
                    it.copy(
                        email = intent.email,
                        emailError = null,
                        emailInfo = null
                    )
                }
            }

            is AuthIntent.AuthCheckChanged -> {
                _state.update {
                    it.copy(
                        check = intent.check,
                        emailError = null,
                        emailInfo = null
                    )
                }
            }

            is AuthIntent.Submit -> {
                val current = _state.value
                signup(current)
            }

            is AuthIntent.PasswordChanged -> {
                _state.update {
                    it.copy(
                        password = intent.value,
                        passwordError = null
                    )
                }
            }

            is AuthIntent.ConfirmPasswordChanged -> {
                _state.update {
                    it.copy(
                        confirmPassword = intent.value,
                        confirmPasswordError = null
                    )
                }
            }

            is AuthIntent.Login -> {
                val current = _state.value
                login(current)
            }

            is AuthIntent.NavigateToLogin -> {
                viewModelScope.launch {
                    _event.emit(AuthEvent.NavigateToLoginScreen)
                }
            }

            is AuthIntent.ToggleLoginMode -> {
                _state.update { it.copy(isOtpLogin = !it.isOtpLogin) }
            }

            is AuthIntent.SendOtp -> {
                val email = _state.value.email

                viewModelScope.launch {
                    runCatching {
                        sendOtpUseCase(email)
                    }
                        .onSuccess {
                        _event.emit(AuthEvent.NavigateToCodeScreen(email))
                        }
                        .onFailure {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    emailError = AuthError.Unknown
                                )
                            }
                        }
                }
            }

            is AuthIntent.NavigateToRegister -> {
                viewModelScope.launch {
                    _event.emit(AuthEvent.NavigateToRegisterScreen)
                }
            }

            is AuthIntent.ContinueAsGuest -> {
                viewModelScope.launch {
                    runCatching { logoutUseCase() }
                    OneSignalManager.logout()
                    _event.emit(AuthEvent.NavigateAsGuest)
                }
            }

            is AuthIntent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(AuthEvent.NavigateBack)
                }
            }

            is AuthIntent.SellerTypeChanged -> {
                _state.update {
                    it.copy(
                        sellerType = intent.type,
                        sellerTypeError = null
                    )
                }
            }

            is AuthIntent.PrivacyPolicyAcceptedChanged -> {
                _state.update {
                    it.copy(
                        privacyPolicyAccepted = intent.accepted,
                        privacyPolicyError = null,
                    )
                }
            }

            AuthIntent.OpenPrivacyPolicy -> {
                _state.update { it.copy(showPrivacyPolicySheet = true) }
            }

            AuthIntent.DismissPrivacyPolicy -> {
                _state.update { it.copy(showPrivacyPolicySheet = false) }
            }
        }
    }
    private fun signup(state: AuthState) {

        val firstNameError = validateFirstName(state.firstName)
        val phoneError = validateNationalPhone(state.phoneNationalDigits)
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)
        val confirmError = validateConfirmPassword(state.password, state.confirmPassword)
        val sellerTypeError = validateSellerType(state.sellerType)
        val privacyPolicyError = validatePrivacyPolicyAccepted(state.privacyPolicyAccepted)

        if (
            firstNameError != null ||
            phoneError != null ||
            emailError != null ||
            passwordError != null ||
            confirmError != null ||
            sellerTypeError != null ||
            privacyPolicyError != null
        ) {
            _state.update {
                it.copy(
                    firstNameError = firstNameError,
                    phoneError = phoneError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmError,
                    sellerTypeError = sellerTypeError,
                    privacyPolicyError = privacyPolicyError,
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            runCatching {
                checkUserExistsUseCase(state.email)
            }
                .onSuccess { exists ->
                    if (exists) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                emailError = AuthError.EmailAlreadyInUse
                            )
                        }
                        return@onSuccess
                    }

                    runCatching {
                        signUpUseCase(
                            email = state.email,
                            password = state.password,
                            firstName = state.firstName,
                            sellerType = state.sellerType!!,
                            phoneE164 = belarusPhoneToE164(state.phoneNationalDigits),
                        )
                    }
                        .onSuccess {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    otpSent = true
                                )
                            }

                            _event.emit(AuthEvent.NavigateToCodeScreen(_state.value.email))
                        }
                        .onFailure {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    emailError = AuthError.Unknown
                                )
                            }
                        }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            emailError = AuthError.Unknown
                        )
                    }
                }
        }
    }
    private fun validateEmail(email: String): AuthError? {
        if (email.isBlank()) return AuthError.EmptyField
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthError.InvalidEmail
        }
        return null
    }
    private fun validatePassword(password: String): AuthError? {
        if (password.isBlank()) return AuthError.PasswordEmpty
        if (password.length < 6) return AuthError.PasswordTooShort
        return null
    }

    private fun validateConfirmPassword(
        password: String,
        confirm: String
    ): AuthError? {
        if (confirm.isBlank()) return AuthError.ConfirmPasswordEmpty
        if (password != confirm) return AuthError.PasswordsNotMatch
        return null
    }

    private fun validateFirstName(name: String): AuthError? {
        if (name.isBlank()) return AuthError.EmptyField
        return null
    }

    private fun validateNationalPhone(nationalDigits: String): AuthError? {
        val d = nationalDigits.filter { it.isDigit() }
        if (d.isEmpty()) return AuthError.EmptyField
        if (d.length != 9) return AuthError.InvalidPhone
        return null
    }

    private fun onOtpEntered(email: String, code: String, check: AuthCheck) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            Log.d("!!!", "chek - ${code} - $email")
            runCatching {
                verifyOtpUseCase(email = email, code = code)
            }.onSuccess {
                Log.d("!!!", "s - $it")
                _state.update { it.copy(isLoading = false, isRegistered = true, code = "", otpError = null) }
                when (check) {
                    AuthCheck.REGISTER -> {
                        OneSignalManager.loginAfterPermission(it.id)
                        _event.emit(AuthEvent.ShowRegistrationSuccess)
                        _event.emit(AuthEvent.NavigateToMain)
                    }
                    AuthCheck.RESET_PASSWORD -> {
                        _event.emit(AuthEvent.NavigateToSetNewPassword)
                    }
                    AuthCheck.LOGIN -> {
                        OneSignalManager.loginAfterPermission(it.id)
                        _event.emit(AuthEvent.NavigateToMain)
                    }
                    AuthCheck.CHANGE_EMAIL_CONFIRM_OLD -> {
                        _event.emit(AuthEvent.NavigateToChangeNewEmail)
                    }
                }
            }.onFailure {
                Log.d("!!!", "chek2 - ${code}")
                Log.d("!!!", "f - $it")
                _state.update { it.copy(isLoading = false, otpError = AuthError.InvalidOtp) }
            }
        }
    }

    private fun login(state: AuthState) {
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)

        if (emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            runCatching {
                signInUseCase(state.email, state.password)
            }
                .onSuccess { user ->
                    Log.d("!!!", "S - user - $user")
                    OneSignalManager.loginAfterPermission(user.id)
                    _state.update {
                        it.copy(isLoading = false)
                    }

                    _event.emit(AuthEvent.NavigateToMain)
                }
                .onFailure {
                    Log.d("!!!", "F - user - $it")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            passwordError = AuthError.InvalidCredentials
                        )
                    }
                }
        }
    }

    private fun validateSellerType(type: SellerType?): AuthError? {
        return if (type == null) AuthError.EmptyField else null
    }

    private fun validatePrivacyPolicyAccepted(accepted: Boolean): AuthError? {
        return if (!accepted) AuthError.PrivacyPolicyNotAccepted else null
    }
}
