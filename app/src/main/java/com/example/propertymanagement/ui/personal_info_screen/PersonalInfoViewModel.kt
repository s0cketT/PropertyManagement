package com.example.propertymanagement.ui.personal_info_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.UpdateUserAvatarUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalInfoViewModel(
    private val updateUserAvatarUseCase: UpdateUserAvatarUseCase
): ViewModel() {

    private val _state = MutableStateFlow(PersonalInfoState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent< PersonalInfoEvent>(viewModelScope)
    val event = _event.flow


    fun processIntent(intent: PersonalInfoIntent) {
        when (intent) {
            is PersonalInfoIntent.SetUser -> {
                _state.update {
                    it.copy(user = intent.user)
                }
            }

            is PersonalInfoIntent.OnBackClick -> {
                _event.emit(PersonalInfoEvent.NavigateBack)
            }

            is PersonalInfoIntent.OnAvatarClick -> {
                _event.emit(PersonalInfoEvent.OpenGallery)
            }

            is PersonalInfoIntent.Save -> {
                saveAvatar()
            }

            is PersonalInfoIntent.AvatarSelected -> {
                _state.update {
                    it.copy(
                        avatarBytes = intent.image,
                        isAvatarRemoved = false
                    )
                }
            }

            is PersonalInfoIntent.RemoveAvatar -> {
                _state.update {
                    it.copy(
                        avatarBytes = null,
                        isAvatarRemoved = true
                    )
                }
            }
        }
    }


    private fun saveAvatar() {
        val currentState = state.value

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            runCatching {

                val newUrl = when {
                    currentState.avatarBytes != null -> {
                        updateUserAvatarUseCase(currentState.avatarBytes)
                    }
                    currentState.isAvatarRemoved -> {
                        updateUserAvatarUseCase.removeAvatar()
                        null
                    }

                    else -> currentState.user?.avatarUrl
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        avatarBytes = null,
                        isAvatarRemoved = false,
                        user = it.user?.copy(avatarUrl = newUrl)
                    )
                }

                _event.emit(PersonalInfoEvent.SaveSuccess)

            }.onFailure {
                _state.update { it.copy(isLoading = false) }
                _event.emit(PersonalInfoEvent.SaveError(it.message))
            }
        }
    }
}