package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.data.model.RateDto
import com.example.propertymanagement.domain.model.CurrencyRate

fun RateDto.toDomain(): CurrencyRate =
    CurrencyRate(
        code = curAbbreviation,
        name = curName,
        scale = curScale,
        ratePerUnit = curOfficialRate / curScale,
        updatedAt = date
    )