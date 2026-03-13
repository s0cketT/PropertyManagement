package com.example.propertymanagement.ui.extensions

fun <T : Comparable<T>> isRangeValid(from: T?, to: T?): Boolean {
    if (from == null || to == null) return true
    return from <= to
}