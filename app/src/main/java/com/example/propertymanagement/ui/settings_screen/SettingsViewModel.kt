package com.example.propertymanagement.ui.settings_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.LogoutUseCase
import com.example.propertymanagement.domain.use_case.ObserveLanguageUseCase
import com.example.propertymanagement.domain.use_case.ObserveThemeUseCase
import com.example.propertymanagement.domain.use_case.SetLanguageUseCase
import com.example.propertymanagement.domain.use_case.SetThemeUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import com.example.propertymanagement.ui.profile_screen.ProfileEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val setLanguageUseCase: SetLanguageUseCase,
    private val observeLanguageUseCase: ObserveLanguageUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val observeThemeUseCase: ObserveThemeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<SettingsEvent>(viewModelScope)
    val event = _event.flow

    init {
        observeLanguage()
        observeTheme()
    }

    private fun observeLanguage() {
        observeLanguageUseCase()
            .onEach { language ->
                _state.update { it.copy(selectedLanguage = language) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeTheme() {
        observeThemeUseCase()
            .onEach { theme ->
                Log.d("THEME", "Observed from DataStore: $theme")
                _state.update { it.copy(selectedTheme = theme) }
            }
            .launchIn(viewModelScope)
    }

    fun processIntent(intent: SettingsIntent) {
        when (intent) {

            is SettingsIntent.SelectLanguage -> {
                _state.update {
                    it.copy(
                        selectedLanguage = intent.language,
                        showLanguageSheet = false
                    )
                }
            }

            is SettingsIntent.ToggleLanguageSheet -> {
                _state.update {
                    it.copy(showLanguageSheet = intent.isVisible)
                }
            }

            is SettingsIntent.ChangeTheme -> {
                Log.d("THEME", "Selected in UI: ${intent.theme}")
                _state.update {
                    it.copy(selectedTheme = intent.theme)
                }
            }

            is SettingsIntent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(SettingsEvent.NavigateBack)
                }
            }

            is SettingsIntent.Save -> {
                Log.d("THEME", "Saving theme: ${state.value.selectedTheme}")
                viewModelScope.launch {
                    setLanguageUseCase(state.value.selectedLanguage)
                    setThemeUseCase(state.value.selectedTheme)
                    _event.emit(SettingsEvent.ApplyTheme)
                }
            }

            is SettingsIntent.Logout -> logout()
        }
    }


    private fun logout() {
        viewModelScope.launch {
            runCatching {
                logoutUseCase()
            }.onSuccess {
                Log.d("!!!", "S - $it")
                _event.emit(SettingsEvent.NavigateToAuth)
            }
                .onFailure {
                    Log.d("!!!", "F - $it")
                }
        }
    }
}