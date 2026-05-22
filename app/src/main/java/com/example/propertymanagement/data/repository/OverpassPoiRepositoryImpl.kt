package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.remote.IOverpassApi
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.domain.repository.IOverpassPoiRepository
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class OverpassPoiRepositoryImpl(
    private val overpassApi: IOverpassApi,
) : IOverpassPoiRepository {

    override suspend fun fetchMapPoisAround(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int,
    ): Result<List<NearbyMapPoi>> = withContext(Dispatchers.IO) {
        runCatching {
            val latStr = String.format(Locale.US, "%.7f", latitude)
            val lonStr = String.format(Locale.US, "%.7f", longitude)
            val r = radiusMeters
            val query = buildString {
                append("[out:json][timeout:25];")
                append("(")
                appendSchoolBlock(this, r, latStr, lonStr)
                appendPolyclinicBlock(this, r, latStr, lonStr)
                appendGroceryBlock(this, r, latStr, lonStr)
                append(");")
                append("out center tags;")
            }

            val dto = overpassApi.interpreter(data = query)
            val elements = dto.elements.orEmpty()
            val out = mutableListOf<NearbyMapPoi>()
            for (el in elements) {
                val type = el.type ?: continue
                val id = el.id ?: continue
                val lat = el.lat ?: el.center?.lat
                val lon = el.lon ?: el.center?.lon
                if (lat == null || lon == null) {
                    continue
                }
                val category = classifyCategory(el.tags) ?: continue
                val name = pickLocalizedName(el.tags)
                out.add(
                    NearbyMapPoi(
                        osmId = id,
                        osmType = type,
                        latitude = lat,
                        longitude = lon,
                        name = name,
                        category = category,
                    ),
                )
            }
            out
        }
    }

    private fun classifyCategory(tags: Map<String, String>?): NearbyPoiCategory? {
        if (tags.isNullOrEmpty()) {
            return null
        }
        when (tags["amenity"]) {
            "school" -> return NearbyPoiCategory.SCHOOL
            "clinic" -> {
                if (isVaccinationOrNonPolyclinicClinic(tags)) {
                    return null
                }
                return NearbyPoiCategory.POLYCLINIC
            }
        }
        if (tags["healthcare"] == "clinic") {
            if (isVaccinationOrNonPolyclinicClinic(tags)) {
                return null
            }
            return NearbyPoiCategory.POLYCLINIC
        }
        when (tags["shop"]) {
            "supermarket", "convenience", "grocery" -> return NearbyPoiCategory.GROCERY
        }
        return null
    }

    /**
     * Исключаем пункты вакцинации и прочие не‑поликлиники при [amenity]=clinic / [healthcare]=clinic.
     */
    private fun isVaccinationOrNonPolyclinicClinic(tags: Map<String, String>): Boolean {
        val healthcare = tags["healthcare"]?.lowercase(Locale.ROOT).orEmpty()
        if (healthcare == "vaccination_centre" || healthcare == "vaccination_center") {
            return true
        }
        val amenity = tags["amenity"]?.lowercase(Locale.ROOT).orEmpty()
        if (amenity == "vaccination_centre" || amenity == "vaccination_center") {
            return true
        }
        if (tags["vaccination"]?.equals("yes", ignoreCase = true) == true) {
            return true
        }
        val speciality = tags["healthcare:speciality"]?.lowercase(Locale.ROOT).orEmpty()
        if (speciality.contains("vaccination")) {
            return true
        }
        return false
    }

    private fun pickLocalizedName(tags: Map<String, String>?): String {
        if (tags.isNullOrEmpty()) {
            return ""
        }
        val direct = tags["name"]
        if (!direct.isNullOrBlank()) {
            return direct.trim()
        }
        val ru = tags["name:ru"]
        if (!ru.isNullOrBlank()) {
            return ru.trim()
        }
        val be = tags["name:be"]
        if (!be.isNullOrBlank()) {
            return be.trim()
        }
        val en = tags["name:en"]
        if (!en.isNullOrBlank()) {
            return en.trim()
        }
        return ""
    }

    private companion object {

        fun appendSchoolBlock(
            sb: StringBuilder,
            r: Int,
            latStr: String,
            lonStr: String,
        ) {
            sb.append("node[\"amenity\"=\"school\"](around:$r,$latStr,$lonStr);")
            sb.append("way[\"amenity\"=\"school\"](around:$r,$latStr,$lonStr);")
            sb.append("relation[\"amenity\"=\"school\"](around:$r,$latStr,$lonStr);")
        }

        /** Только поликлиники / амбулатории: [amenity]=clinic и объекты с [healthcare]=clinic. Без [amenity]=doctors. */
        fun appendPolyclinicBlock(
            sb: StringBuilder,
            r: Int,
            latStr: String,
            lonStr: String,
        ) {
            sb.append("node[\"amenity\"=\"clinic\"](around:$r,$latStr,$lonStr);")
            sb.append("way[\"amenity\"=\"clinic\"](around:$r,$latStr,$lonStr);")
            sb.append("relation[\"amenity\"=\"clinic\"](around:$r,$latStr,$lonStr);")
            sb.append("node[\"healthcare\"=\"clinic\"](around:$r,$latStr,$lonStr);")
            sb.append("way[\"healthcare\"=\"clinic\"](around:$r,$latStr,$lonStr);")
            sb.append("relation[\"healthcare\"=\"clinic\"](around:$r,$latStr,$lonStr);")
        }

        fun appendGroceryBlock(
            sb: StringBuilder,
            r: Int,
            latStr: String,
            lonStr: String,
        ) {
            for (shop in listOf("supermarket", "convenience", "grocery")) {
                sb.append("node[\"shop\"=\"$shop\"](around:$r,$latStr,$lonStr);")
                sb.append("way[\"shop\"=\"$shop\"](around:$r,$latStr,$lonStr);")
                sb.append("relation[\"shop\"=\"$shop\"](around:$r,$latStr,$lonStr);")
            }
        }
    }
}
