package com.example.propertymanagement.ui.personal_info_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.propertymanagement.domain.use_case.GetUserProfileUseCase
import com.example.propertymanagement.domain.use_case.UpdateUserAvatarUseCase
import com.example.propertymanagement.domain.use_case.UpdateUserProfileUseCase
import com.example.propertymanagement.ui.SingleFlowEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalInfoViewModel(
    private val updateUserAvatarUseCase: UpdateUserAvatarUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PersonalInfoState())
    val state = _state.asStateFlow()

    private val _event = SingleFlowEvent<PersonalInfoEvent>(viewModelScope)
    val event = _event.flow

    fun processIntent(intent: PersonalInfoIntent) {
        when (intent) {
            is PersonalInfoIntent.SetUser -> {
                val u = intent.user
                _state.update {
                    it.copy(
                        user = u,
                        editedName = u.name,
                        editedPhoneNational = BelarusPhoneUtils.toNationalDigits(u.phone),
                        editedSellerType = u.sellerType,
                        nameInvalid = false,
                        phoneInvalid = false
                    )
                }
            }

            is PersonalInfoIntent.OnBackClick -> {
                _event.emit(PersonalInfoEvent.NavigateBack)
            }

            is PersonalInfoIntent.OnAvatarClick -> {
                _event.emit(PersonalInfoEvent.OpenGallery)
            }

            is PersonalInfoIntent.Save -> {
                save()
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

            is PersonalInfoIntent.NameChanged -> {
                _state.update {
                    it.copy(editedName = intent.value, nameInvalid = false)
                }
            }

            is PersonalInfoIntent.PhoneNationalChanged -> {
                val digits = intent.value.filter { it.isDigit() }
                    .take(BelarusPhoneUtils.NATIONAL_DIGIT_COUNT)
                _state.update {
                    it.copy(editedPhoneNational = digits, phoneInvalid = false)
                }
            }

            is PersonalInfoIntent.SellerTypeChanged -> {
                _state.update { it.copy(editedSellerType = intent.type) }
            }
        }
    }

    private fun save() {
        val current = state.value
        val user = current.user ?: return
        val name = current.editedName.trim()
        if (name.isEmpty()) {
            _state.update { it.copy(nameInvalid = true, phoneInvalid = false) }
            return
        }
        if (!BelarusPhoneUtils.isValidNational(current.editedPhoneNational)) {
            _state.update { it.copy(phoneInvalid = true, nameInvalid = false) }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    nameInvalid = false,
                    phoneInvalid = false
                )
            }

            runCatching {
                val e164 = BelarusPhoneUtils.toE164(current.editedPhoneNational)
                updateUserProfileUseCase(name, e164, current.editedSellerType)

                val newUrl = when {
                    current.avatarBytes != null -> {
                        updateUserAvatarUseCase(current.avatarBytes)
                    }

                    current.isAvatarRemoved -> {
                        updateUserAvatarUseCase.removeAvatar()
                        null
                    }

                    else -> current.user.avatarUrl
                }

                val refreshed = getUserProfileUseCase(user.id)
                _state.update {
                    it.copy(
                        isLoading = false,
                        avatarBytes = null,
                        isAvatarRemoved = false,
                        user = refreshed,
                        editedName = refreshed.name,
                        editedPhoneNational = BelarusPhoneUtils.toNationalDigits(refreshed.phone),
                        editedSellerType = refreshed.sellerType
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
