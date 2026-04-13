package com.example.propertymanagement.data.mapper

import com.example.propertymanagement.domain.model.CeilingHeightType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.domain.model.RoomsType


fun CeilingHeightType.toDbValue(): Double = when (this) {
    CeilingHeightType.H_2_5 -> 2.5
    CeilingHeightType.H_2_7 -> 2.7
    CeilingHeightType.H_3_0 -> 3.0
    CeilingHeightType.H_3_5 -> 3.5
    CeilingHeightType.H_4_0 -> 4.0
}
fun Double.toCeilingHeightType(): CeilingHeightType =
    CeilingHeightType.entries.firstOrNull { it.toDbValue() == this }
        ?: CeilingHeightType.H_2_5

fun Int.toRoomsType(): RoomsType? = when (this) {
    1 -> RoomsType.ONE
    2 -> RoomsType.TWO
    3 -> RoomsType.THREE
    4 -> RoomsType.FOUR
    5 -> RoomsType.FIVE
    6 -> RoomsType.FIVE_PLUS
    else -> null
}

fun RoomsType.toInt(): Int = when (this) {
    RoomsType.ONE -> 1
    RoomsType.TWO -> 2
    RoomsType.THREE -> 3
    RoomsType.FOUR -> 4
    RoomsType.FIVE -> 5
    RoomsType.FIVE_PLUS -> 6
}

inline fun <reified T : Enum<T>> String?.fromDb(): T? {
    return this?.let { runCatching { enumValueOf<T>(it) }.getOrNull() }
}

fun Enum<*>.toDbValue(): String = name

fun DealType.toPropertyStatus(): PropertyStatus {
    return when (this) {
        DealType.BUY -> PropertyStatus.FOR_SALE
        DealType.RENT -> PropertyStatus.FOR_RENT
    }
}


