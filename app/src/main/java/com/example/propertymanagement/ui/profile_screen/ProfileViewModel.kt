package com.example.propertymanagement.ui.profile_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetUserProfileUseCase
import com.example.propertymanagement.domain.use_case.SaveAppRatingUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveAppRatingUseCase: SaveAppRatingUseCase
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
            ProfileIntent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(ProfileEvent.NavigateBack)
                }
            }
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
            ProfileIntent.MyApplications -> {
                viewModelScope.launch {
                    _event.emit(ProfileEvent.NavigateToMyApplications)
                }
            }
            ProfileIntent.RateApp -> {
                _state.update { it.copy(isRateAppSheetOpen = true) }
            }
            ProfileIntent.DismissRateAppSheet -> {
                _state.update {
                    it.copy(
                        isRateAppSheetOpen = false,
                        isSavingAppRating = false
                    )
                }
            }
            is ProfileIntent.SubmitAppRating -> submitAppRating(intent.stars)
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
                    isLoading = false,
                    isRateAppSheetOpen = _state.value.isRateAppSheetOpen,
                    isSavingAppRating = _state.value.isSavingAppRating
                )
            }.onFailure {
                Log.d("!!!", "F - $it")
                _state.value = ProfileState(
                    user = null,
                    isLoading = false,
                    isRateAppSheetOpen = false
                )
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _event.emit(ProfileEvent.NavigateToAuth)
        }
    }

    private fun submitAppRating(stars: Int) {
        if (stars !in 1..5) return
        viewModelScope.launch {
            _state.update { it.copy(isSavingAppRating = true) }
            runCatching { saveAppRatingUseCase(stars) }
                .onSuccess {
                    _state.update { s ->
                        val u = s.user
                        s.copy(
                            isRateAppSheetOpen = false,
                            isSavingAppRating = false,
                            user = u?.copy(appRating = stars)
                        )
                    }
                    _event.emit(ProfileEvent.AppRatingSaved)
                }
                .onFailure {
                    _state.update { it.copy(isSavingAppRating = false) }
                    _event.emit(ProfileEvent.AppRatingSaveFailed)
                }
        }
    }
}