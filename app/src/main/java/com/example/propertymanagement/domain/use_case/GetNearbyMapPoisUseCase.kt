package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.domain.repository.IOverpassPoiRepository

class GetNearbyMapPoisUseCase(
    private val overpassPoiRepository: IOverpassPoiRepository,
) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int = DEFAULT_RADIUS_METERS,
    ): Resource<List<NearbyMapPoi>, String> {
        val result = overpassPoiRepository.fetchMapPoisAround(
            latitude = latitude,
            longitude = longitude,
            radiusMeters = radiusMeters,
        )
        return result.fold(
            onSuccess = { Resource.Success(it) },
            onFailure = { Resource.Error(it.message ?: "Overpass request failed") },
        )
    }

    companion object {
        const val DEFAULT_RADIUS_METERS = 1_500
    }
}
