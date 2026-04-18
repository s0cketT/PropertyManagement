package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.repository.IUserRepository

class UpdateUserProfileUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(
        name: String,
        phone: String,
        sellerType: SellerType
    ) {
        userRepository.updateUserProfile(
            name = name,
            phone = phone,
            sellerType = sellerType
        )
    }
}
