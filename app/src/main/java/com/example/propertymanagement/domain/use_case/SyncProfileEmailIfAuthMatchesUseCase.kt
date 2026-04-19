package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.AuthRepository
import com.example.propertymanagement.domain.repository.IUserRepository

/**
 * После подтверждения смены почты по ссылке из письма: обновляет сессию, тянет профиль с сервера и,
 * если email в Auth совпадает с ожидаемым и смена не «в подвешенном» состоянии, синхронизирует `users.email`.
 */
class SyncProfileEmailIfAuthMatchesUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(expectedEmail: String): Result<Unit> {
        val want = expectedEmail.trim().lowercase()
        if (want.isBlank()) return Result.failure(IllegalArgumentException("empty"))

        runCatching { authRepository.refreshAuthSession() }

        val authUser = authRepository.retrieveCurrentUserFromServer()
            ?: return Result.failure(IllegalStateException("not_confirmed"))

        val actual = authUser.email?.trim()?.lowercase().orEmpty()
        if (actual != want) {
            return Result.failure(IllegalStateException("not_confirmed"))
        }

        val pendingNew = authUser.newEmail?.trim().orEmpty()
        if (pendingNew.isNotEmpty()) {
            return Result.failure(IllegalStateException("not_confirmed"))
        }

        return runCatching {
            userRepository.updateUserEmail(expectedEmail.trim())
        }
    }
}
