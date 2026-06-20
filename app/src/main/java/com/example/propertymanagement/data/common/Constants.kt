package com.example.propertymanagement.data.common

object Constants {

    const val API_KEY_MAPKIT = "e8dadba5-8abb-4655-b3a1-eb10a5085a55"
    const val API_KEY_GEOCODER = "84851e39-9a79-4d16-99d2-0f0ed0f6049f"
    const val API_KEY_GEOSUGGEST = "84851e39-9a79-4d16-99d2-0f0ed0f6049f"

    /** Запасная точка карты (Минск), если нет геолокации. */
    const val MAP_PICKER_FALLBACK_LAT = 53.902284
    const val MAP_PICKER_FALLBACK_LON = 27.561831
    const val MAP_PICKER_INITIAL_ZOOM = 11f
    const val MAP_PICKER_SAVED_POINT_ZOOM = 16f

    const val BASE_URL_SUPABASE = "https://igpxiyulvaqgivpgfmnf.supabase.co"
    const val ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlncHhpeXVsdmFxZ2l2cGdmbW5mIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzQ2NzcwOTksImV4cCI6MjA5MDI1MzA5OX0.UG_IoQPQUapWQgYQ8QSKtguqmHF4XiV6Jw3sEiJWU6Y"

    const val BUCKET_IMAGES = "property-images"
    const val PROPERTY_IMAGE_PREFIX = "property_"
    const val IMAGE_EXTENSION = ".jpg"

    const val ONESIGNAL_APP_ID = "d9f894cc-f806-42fd-a048-7e14097e8ff1"
}