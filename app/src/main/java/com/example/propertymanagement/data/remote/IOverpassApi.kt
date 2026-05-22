package com.example.propertymanagement.data.remote

import com.example.propertymanagement.data.remote.overpass.OverpassResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Клиент Overpass API (OpenStreetMap). Только HTTPS POST form data.
 * https://wiki.openstreetmap.org/wiki/Overpass_API
 *
 * Интерфейс [internal]: возвращаемые DTO не должны «протекать» в публичный API модуля.
 */
internal interface IOverpassApi {

    @FormUrlEncoded
    @POST("api/interpreter")
    suspend fun interpreter(
        @Field("data") data: String,
    ): OverpassResponseDto
}
