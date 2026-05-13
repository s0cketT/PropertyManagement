package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.repository.IManagerCommissionRepository

class GetManagerCommissionPercentUseCase(
    private val repository: IManagerCommissionRepository,
) {

    /** Процент из БД; при ошибке — 0 (показываем цену без надбавки). */
    suspend operator fun invoke(): Double {
        return when (val result = repository.fetchCommissionPercent()) {
            is Resource.Success -> result.data
            is Resource.Error -> 0.0
        }
    }
}
