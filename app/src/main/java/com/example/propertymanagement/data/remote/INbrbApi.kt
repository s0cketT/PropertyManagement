package com.example.propertymanagement.data.remote

import com.example.propertymanagement.data.model.RateDto
import retrofit2.http.GET
import retrofit2.http.Query

interface INbrbApi {
    @GET("exrates/rates")
    suspend fun getTodayRates(
        @Query("periodicity") periodicity: Int = 0
    ): List<RateDto>
}