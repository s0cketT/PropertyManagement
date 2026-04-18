package com.example.propertymanagement.ui.personal_info_screen

import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.UserProfile

data class PersonalInfoState(
    val user: UserProfile? = null,
    val editedName: String = "",
    val editedPhoneNational: String = "",
    val editedSellerType: SellerType = SellerType.OWNER,
    val nameInvalid: Boolean = false,
    val phoneInvalid: Boolean = false,
    val isLoading: Boolean = false,
    val avatarBytes: ByteArray? = null,
    val isAvatarRemoved: Boolean = false
)
