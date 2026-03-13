package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.mapper.toDomain
import com.example.propertymanagement.data.remote.INbrbApi
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.ExceptionDomainModel
import com.example.propertymanagement.domain.repository.ICurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyRepositoryImpl(
    private val apiService: INbrbApi
) : ICurrencyRepository {

    override suspend fun getTodayRates(): Resource<Map<String, CurrencyRate>, ExceptionDomainModel> =
        withContext(Dispatchers.IO) {
            runCatching {
                val dtos = apiService.getTodayRates(periodicity = 0)

                val rates = dtos
                    .filter { it.curAbbreviation in listOf("USD", "EUR") }
                    .map { it.toDomain() }
                    .associateBy { it.code }

                // Добавляем BYN как базовую валюту
                val result = rates + ("BYN" to CurrencyRate.BYN)

                Resource.Success<Map<String, CurrencyRate>, ExceptionDomainModel>(result)
            }.getOrElse { throwable ->
                Resource.Error(
                    ExceptionDomainModel.from(throwable)
                )
            }
        }


}