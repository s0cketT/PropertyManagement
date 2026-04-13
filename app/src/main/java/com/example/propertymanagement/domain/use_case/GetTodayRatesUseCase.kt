package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.ExceptionDomainModel
import com.example.propertymanagement.domain.repository.ICurrencyRepository

class GetTodayRatesUseCase(
    private val currencyRepository: ICurrencyRepository
) {
    suspend operator fun invoke(): Resource<Map<String, CurrencyRate>, ExceptionDomainModel> {
        return currencyRepository.getTodayRates()
    }
}