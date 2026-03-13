package com.example.propertymanagement.domain.use_case

import android.location.Location
import com.example.propertymanagement.domain.repository.ILocationRepository
import kotlinx.coroutines.flow.Flow

class ObserveLocationUseCase(
    private val ILocationRepository: ILocationRepository
) {
    operator fun invoke(): Flow<Location> {
        return ILocationRepository.observeLocation()
    }
}