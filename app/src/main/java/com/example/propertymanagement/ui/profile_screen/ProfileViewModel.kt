package com.example.propertymanagement.ui.profile_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetUserProfileUseCase
import com.example.propertymanagement.domain.use_case.LogoutUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<ProfileEvent>(viewModelScope)
    val event = _event.flow

    init {
        checkAuth()
    }

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoginClick -> login()
            ProfileIntent.NavToPublish -> {
                viewModelScope.launch {
                    _event.emit(ProfileEvent.NavigateToPublishScreen)
                }
            }
            ProfileIntent.MyAds -> {
                viewModelScope.launch {
                    _event.emit(ProfileEvent.NavigateToMyAds)
                }
            }
            ProfileIntent.RateApp -> {
                viewModelScope.launch {
                    _event.emit(ProfileEvent.RateApp)
                }
            }
            ProfileIntent.Settings -> {
                viewModelScope.launch {
                    _event.emit(ProfileEvent.NavigateToSettings)
                }
            }

            ProfileIntent.PersonalInfo -> {
                viewModelScope.launch {
                    _event.emit(ProfileEvent.NavigateToPersonalInfo)
                }
            }

        }
    }

    private fun checkAuth() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            runCatching {
                val authUser = getCurrentUserUseCase()
                val profile = getUserProfileUseCase(authUser?.id ?: "")
                profile
            }.onSuccess { user ->
                Log.d("!!!", "S - $user")
                _state.value = ProfileState(
                    user = user,
                    isLoading = false
                )
            }.onFailure {
                Log.d("!!!", "F - $it")
                _state.value = ProfileState(
                    user = null,
                    isLoading = false
                )
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _event.emit(ProfileEvent.NavigateToAuth)
        }
    }
}