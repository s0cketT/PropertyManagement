package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.IUserRepository

class CheckUserExistsUseCase(
    private val IUserRepository: IUserRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return IUserRepository.isUserExists(email)
    }
}