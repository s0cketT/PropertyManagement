package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.ExceptionDomainModel

fun interface IManagerCommissionRepository {

    suspend fun fetchCommissionPercent(): Resource<Double, ExceptionDomainModel>
}
