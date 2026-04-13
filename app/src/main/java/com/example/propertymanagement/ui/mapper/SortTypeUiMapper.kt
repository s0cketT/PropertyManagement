package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.SortType
import com.example.propertymanagement.R

fun SortType.titleRes(): Int = when (this) {
    SortType.NEWEST -> R.string.sort_newest
    SortType.PRICE_ASC -> R.string.sort_price_asc
    SortType.PRICE_DESC -> R.string.sort_price_desc
}