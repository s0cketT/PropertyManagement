package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.GeosuggestAddressField
import com.example.propertymanagement.domain.model.GeosuggestItem

interface IYandexGeosuggestRepository {

    suspend fun fetchSuggestions(
        field: GeosuggestAddressField,
        query: String,
    ): Result<List<GeosuggestItem>>
}
