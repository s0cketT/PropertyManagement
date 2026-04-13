package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.IStorageRepository
import com.example.propertymanagement.domain.repository.IUserRepository


class UpdateUserAvatarUseCase(
    private val userRepository: IUserRepository,
    private val storageRepository: IStorageRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): String {
        var uploadedUrl: String? = null

        return runCatching {
            uploadedUrl = storageRepository.uploadImage(imageBytes)
            userRepository.updateAvatar(uploadedUrl)
            uploadedUrl
        }.onFailure {
            uploadedUrl?.let {
                runCatching { storageRepository.deleteImage(it) }
            }
        }.getOrThrow()
    }


    suspend fun removeAvatar() {
        val currentUrl = userRepository.getCurrentAvatarUrl() ?: return

        runCatching {
            userRepository.updateAvatar(null)
            storageRepository.deleteImage(currentUrl)
        }
    }
}