package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.GeosuggestAddressContext
import com.example.propertymanagement.domain.model.GeosuggestAddressField
import com.example.propertymanagement.domain.model.GeosuggestItem
import com.example.propertymanagement.domain.repository.IYandexGeosuggestRepository

class GetGeosuggestUseCase(
    private val yandexGeosuggestRepository: IYandexGeosuggestRepository,
) {

    suspend operator fun invoke(
        field: GeosuggestAddressField,
        query: String,
        addressContext: GeosuggestAddressContext,
    ): Resource<List<GeosuggestItem>, String> {
        val searchText = buildSearchText(
            field = field,
            query = query,
            addressContext = addressContext,
        )
        val result = yandexGeosuggestRepository.fetchSuggestions(
            field = field,
            query = searchText,
        )
        return result.fold(
            onSuccess = { Resource.Success(it) },
            onFailure = { Resource.Error(it.message ?: "Geosuggest request failed") },
        )
    }

    private fun buildSearchText(
        field: GeosuggestAddressField,
        query: String,
        addressContext: GeosuggestAddressContext,
    ): String {
        val trimmedQuery = query.trim()
        return when (field) {
            GeosuggestAddressField.COUNTRY -> trimmedQuery
            GeosuggestAddressField.REGION -> joinAddressParts(
                addressContext.country,
                trimmedQuery,
            )
            GeosuggestAddressField.CITY -> joinAddressParts(
                addressContext.country,
                addressContext.region,
                trimmedQuery,
            )
            GeosuggestAddressField.STREET -> joinAddressParts(
                addressContext.city,
                trimmedQuery,
            )
        }
    }

    private fun joinAddressParts(vararg parts: String): String {
        return parts
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(separator = " ")
    }
}
