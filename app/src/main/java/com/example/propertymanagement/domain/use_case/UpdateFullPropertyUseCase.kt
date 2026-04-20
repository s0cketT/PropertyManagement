package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.CreateProperty
import com.example.propertymanagement.domain.repository.IPropertyRepository
import com.example.propertymanagement.domain.repository.IStorageRepository
import kotlinx.coroutines.coroutineScope

class UpdateFullPropertyUseCase(
    private val propertyRepository: IPropertyRepository,
    private val storageRepository: IStorageRepository,
) {
    suspend operator fun invoke(
        propertyId: Int,
        request: CreateProperty,
        newImageBytes: List<ByteArray>,
        removeExistingImagesFirst: Boolean,
    ) = coroutineScope {
        propertyRepository.updateFullProperty(propertyId, request)
        if (removeExistingImagesFirst) {
            propertyRepository.clearPropertyImages(propertyId)
        }
        if (newImageBytes.isEmpty()) return@coroutineScope

        val uploadedUrls = storageRepository.uploadImages(newImageBytes)
        propertyRepository.saveImages(propertyId, uploadedUrls)
    }
}
