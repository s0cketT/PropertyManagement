package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.common.Constants.BUCKET_IMAGES
import com.example.propertymanagement.data.common.Constants.IMAGE_EXTENSION
import com.example.propertymanagement.data.common.Constants.PROPERTY_IMAGE_PREFIX
import com.example.propertymanagement.domain.repository.IStorageRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import java.util.UUID
class StorageRepositoryImpl(
    private val supabase: SupabaseClient
) : IStorageRepository {

    override suspend fun uploadImages(images: List<ByteArray>): List<String> {
        return images.map { bytes ->

            val fileName = buildFileName()

            supabase.storage
                .from(BUCKET_IMAGES)
                .upload(fileName, bytes)

            supabase.storage
                .from(BUCKET_IMAGES)
                .publicUrl(fileName)
        }
    }

    override suspend fun uploadImage(image: ByteArray): String {
        val fileName = buildFileName()

        supabase.storage
            .from(BUCKET_IMAGES)
            .upload(fileName, image)

        return supabase.storage
            .from(BUCKET_IMAGES)
            .publicUrl(fileName)
    }

    override suspend fun deleteImage(url: String) {
        val fileName = url.substringAfterLast("/")

        supabase.storage
            .from(BUCKET_IMAGES)
            .delete(listOf(fileName))
    }

    private fun buildFileName(): String {
        return PROPERTY_IMAGE_PREFIX +
                UUID.randomUUID() + IMAGE_EXTENSION
    }
}