package com.example.propertymanagement.domain.model

data class RangeFilter<T>(
    val from: T? = null,
    val to: T? = null
) {

    fun isEmpty(): Boolean {
        return from == null && to == null
    }

    fun clear(): RangeFilter<T> {
        return RangeFilter(null, null)
    }
}

typealias IntRangeFilter = RangeFilter<Int>
typealias StringRangeFilter = RangeFilter<String>
