package com.example.propertymanagement.domain.use_case

import android.location.Location
import com.example.propertymanagement.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class ObserveLocationUseCase(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<Location> {
        return locationRepository.observeLocation()
    }
}