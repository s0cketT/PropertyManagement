package com.example.propertymanagement.domain.repository


import android.location.Location
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun observeLocation(): Flow<Location>
}