package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.common.Constants
import com.example.propertymanagement.domain.model.GeosuggestAddressField
import com.example.propertymanagement.domain.model.GeosuggestItem
import com.example.propertymanagement.domain.repository.IYandexGeosuggestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

internal class YandexGeosuggestRepositoryImpl : IYandexGeosuggestRepository {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    override suspend fun fetchSuggestions(
        field: GeosuggestAddressField,
        query: String,
    ): Result<List<GeosuggestItem>> = withContext(Dispatchers.IO) {
        val trimmed = query.trim()
        if (trimmed.length < MIN_QUERY_LENGTH) {
            return@withContext Result.success(emptyList())
        }

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("suggest-maps.yandex.ru")
            .addPathSegments("v1/suggest")
            .addQueryParameter("apikey", Constants.API_KEY_GEOSUGGEST)
            .addQueryParameter("text", trimmed)
            .addQueryParameter("lang", "ru_RU")
            .addQueryParameter("types", field.toSuggestTypes())
            .addQueryParameter("results", MAX_RESULTS.toString())
            .build()

        val request = Request.Builder().url(url).get().build()
        try {
            httpClient.newCall(request).execute().use { response ->
                val body = response.body?.string()
                    ?: return@withContext Result.failure(IllegalStateException("Empty geosuggest response"))
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        IllegalStateException("Geosuggest request failed with HTTP ${response.code}"),
                    )
                }
                Result.success(parseSuggestions(JSONObject(body)))
            }
        } catch (exception: Exception) {
            Result.failure(
                IllegalStateException("Geosuggest request failed", exception),
            )
        }
    }

    private fun GeosuggestAddressField.toSuggestTypes(): String {
        return when (this) {
            GeosuggestAddressField.COUNTRY -> "country"
            GeosuggestAddressField.REGION -> "province,area"
            GeosuggestAddressField.CITY -> "locality"
            GeosuggestAddressField.STREET -> "street"
        }
    }

    private fun parseSuggestions(json: JSONObject): List<GeosuggestItem> {
        val results = json.optJSONArray("results") ?: return emptyList()
        val suggestions = mutableListOf<GeosuggestItem>()

        for (index in 0 until results.length()) {
            val item = results.optJSONObject(index) ?: continue
            val title = item.optJSONObject("title")?.optString("text")?.trim().orEmpty()
            if (title.isEmpty()) {
                continue
            }
            val subtitle = item.optJSONObject("subtitle")?.optString("text")?.trim()?.ifBlank { null }
            suggestions.add(
                GeosuggestItem(
                    title = title,
                    subtitle = subtitle,
                ),
            )
        }

        return suggestions.distinctBy { it.title }
    }

    private companion object {
        const val MIN_QUERY_LENGTH = 2
        const val MAX_RESULTS = 7
    }
}
