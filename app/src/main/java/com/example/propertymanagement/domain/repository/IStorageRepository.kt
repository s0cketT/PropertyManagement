package com.example.propertymanagement.domain.repository

interface IStorageRepository {
    suspend fun uploadImages(images: List<ByteArray>): List<String>
    suspend fun uploadImage(image: ByteArray): String
    suspend fun deleteImage(url: String)
}