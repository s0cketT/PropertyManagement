package com.example.propertymanagement.domain.repository


import com.example.propertymanagement.domain.model.CreateProperty
import com.example.propertymanagement.domain.model.Property

interface IPropertyRepository {

    suspend fun getProperties(userId: String?): List<Property>

    suspend fun getMyProperties(userId: String): List<Property>

    suspend fun createProperty(
        request: CreateProperty
    ): Int

    suspend fun saveImages(
        propertyId: Int,
        imageUrls: List<String>
    )

    suspend fun deleteProperty(propertyId: Int)

    suspend fun submitPropertyApplication(
        propertyId: Int,
        applicantUserId: String,
        comment: String?
    )
}