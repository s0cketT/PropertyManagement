package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.CityDto
import com.example.propertymanagement.data.model.RegionDto
import com.example.propertymanagement.domain.model.City
import com.example.propertymanagement.domain.model.Region

fun RegionDto.toDomain(): Region {
    return Region(
        id = id,
        name = name,
    )
}

fun CityDto.toDomain(): City {
    return City(
        id = id,
        regionId = region_id,
        name = name,
        lat = lat,
        lng = lng,
    )
}

