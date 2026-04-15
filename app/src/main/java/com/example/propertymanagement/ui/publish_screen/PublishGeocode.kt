package com.example.propertymanagement.ui.publish_screen

import com.example.propertymanagement.data.common.Constants
import com.example.propertymanagement.domain.model.GeocodedAddressParts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Прямое и обратное геокодирование через [HTTP API Яндекс.Геокодера](https://yandex.ru/dev/maps/geocoder/).
 */
private val yandexGeocodeClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
}

suspend fun geocodeAddressQuery(query: String): Pair<Double, Double>? = withContext(Dispatchers.IO) {
    val trimmed = query.trim()
    if (trimmed.isEmpty()) return@withContext null
    val url = HttpUrl.Builder()
        .scheme("https")
        .host("geocode-maps.yandex.ru")
        .addPathSegment("1.x")
        .addQueryParameter("apikey", Constants.API_KEY_GEOCODER)
        .addQueryParameter("geocode", trimmed)
        .addQueryParameter("format", "json")
        .addQueryParameter("lang", "ru_RU")
        .addQueryParameter("results", "1")
        .build()
    val json = executeGeocodeRequest(url) ?: return@withContext null
    val geoObject = firstGeoObject(json) ?: return@withContext null
    val pos = geoObject.optJSONObject("Point")?.optString("pos")?.trim() ?: return@withContext null
    val parts = pos.split(Regex("\\s+"))
    if (parts.size < 2) return@withContext null
    val lon = parts[0].toDoubleOrNull() ?: return@withContext null
    val lat = parts[1].toDoubleOrNull() ?: return@withContext null
    Pair(lat, lon)
}

suspend fun reverseGeocodeCoordinates(
    latitude: Double,
    longitude: Double
): GeocodedAddressParts = withContext(Dispatchers.IO) {
    val geocodeParam = "$longitude,$latitude"
    val url = HttpUrl.Builder()
        .scheme("https")
        .host("geocode-maps.yandex.ru")
        .addPathSegment("1.x")
        .addQueryParameter("apikey", Constants.API_KEY_GEOCODER)
        .addQueryParameter("geocode", geocodeParam)
        .addQueryParameter("format", "json")
        .addQueryParameter("lang", "ru_RU")
        .addQueryParameter("results", "1")
        .build()
    val json = executeGeocodeRequest(url) ?: return@withContext GeocodedAddressParts()
    val geoObject = firstGeoObject(json) ?: return@withContext GeocodedAddressParts()
    parseAddressFromGeoObject(geoObject)
}

private fun executeGeocodeRequest(url: HttpUrl): JSONObject? {
    val request = Request.Builder().url(url).get().build()
    return try {
        yandexGeocodeClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            JSONObject(body)
        }
    } catch (_: Exception) {
        null
    }
}

private fun firstGeoObject(root: JSONObject): JSONObject? {
    val collection = root.optJSONObject("response")
        ?.optJSONObject("GeoObjectCollection") ?: return null
    val members = collection.optJSONArray("featureMember") ?: return null
    if (members.length() == 0) return null
    return members.getJSONObject(0).optJSONObject("GeoObject")
}

private fun parseAddressFromGeoObject(geoObject: JSONObject): GeocodedAddressParts {
    val meta = geoObject.optJSONObject("metaDataProperty")
        ?.optJSONObject("GeocoderMetaData") ?: return GeocodedAddressParts()
    val address = meta.optJSONObject("Address") ?: return GeocodedAddressParts()
    val components = address.optJSONArray("Components") ?: return GeocodedAddressParts()
    return parseComponents(components)
}

private fun parseComponents(components: JSONArray): GeocodedAddressParts {
    var country = ""
    var region = ""
    var city = ""
    var street = ""
    var house = ""
    for (i in 0 until components.length()) {
        val comp = components.optJSONObject(i) ?: continue
        val kind = comp.optString("kind", "")
        val name = comp.optString("name", "")
        when (kind) {
            "country" -> country = name
            "province", "area", "administrative_area" ->
                if (region.isBlank()) region = name
            "locality", "district" ->
                if (city.isBlank()) city = name
            "street" -> street = name
            "house" -> house = name
        }
    }
    return GeocodedAddressParts(
        country = country,
        region = region,
        city = city,
        street = street,
        house = house
    )
}

fun PublishState.buildAddressQueryString(): String {
    return listOf(
        addressCountry.trim(),
        addressRegion.trim(),
        addressCity.trim(),
        addressStreet.trim(),
        addressHouse.trim()
    )
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")
}
