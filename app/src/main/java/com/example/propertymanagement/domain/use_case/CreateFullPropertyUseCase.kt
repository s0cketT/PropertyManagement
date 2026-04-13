package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.CreateProperty
import com.example.propertymanagement.domain.repository.IPropertyRepository
import com.example.propertymanagement.domain.repository.IStorageRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class CreateFullPropertyUseCase(
    private val propertyRepository: IPropertyRepository,
    private val storageRepository: IStorageRepository
) {
    suspend operator fun invoke(
        request: CreateProperty,
        imageBytes: List<ByteArray>
    ): Int = coroutineScope {

        var propertyId: Int? = null
        var uploadedUrls: List<String> = emptyList()

        runCatching {
            propertyId = propertyRepository.createProperty(request)
            uploadedUrls = storageRepository.uploadImages(imageBytes)
            propertyRepository.saveImages(propertyId, uploadedUrls)
            propertyId
        }.onFailure {
            propertyId?.let {
                runCatching { propertyRepository.deleteProperty(it) }
            }
            if (uploadedUrls.isNotEmpty()) {
                uploadedUrls.map {
                    async { runCatching { storageRepository.deleteImage(it) } }
                }.awaitAll()
            }
        }.getOrThrow()
    }
}