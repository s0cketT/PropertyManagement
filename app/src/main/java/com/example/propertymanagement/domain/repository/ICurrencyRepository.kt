package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.ExceptionDomainModel
import kotlinx.coroutines.flow.Flow

interface ICurrencyRepository {
    suspend fun getTodayRates(): Resource<Map<String, CurrencyRate>, ExceptionDomainModel>
}
