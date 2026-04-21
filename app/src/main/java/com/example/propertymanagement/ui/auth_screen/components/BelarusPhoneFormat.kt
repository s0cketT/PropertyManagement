package com.example.propertymanagement.ui.auth_screen.components

private const val MAX_NATIONAL_DIGITS = 9

const val BELARUS_PHONE_FORMATTED_LENGTH = 17

private val BELARUS_FORMAT_DIGIT_CHAR_INDEX = intArrayOf(5, 6, 8, 9, 11, 12, 14, 15, 16)

fun belarusPhoneCaretPositionAfterDigitCount(digitCount: Int): Int {
    require(digitCount in 0..MAX_NATIONAL_DIGITS)
    if (digitCount <= 0) {
        return BELARUS_FORMAT_DIGIT_CHAR_INDEX[0]
    }
    if (digitCount >= MAX_NATIONAL_DIGITS) {
        return BELARUS_PHONE_FORMATTED_LENGTH
    }
    val lastDigitIndex = digitCount - 1
    return BELARUS_FORMAT_DIGIT_CHAR_INDEX[lastDigitIndex] + 1
}

fun formatBelarusPhoneDisplay(nationalDigits: String): String {
    val d = nationalDigits.filter { it.isDigit() }.take(MAX_NATIONAL_DIGITS)
    fun segment(start: Int, len: Int): String {
        if (start >= d.length) {
            return "_".repeat(len)
        }
        val end = (start + len).coerceAtMost(d.length)
        val filled = d.substring(start, end)
        val pad = "_".repeat(len - filled.length)
        return filled + pad
    }
    val a = segment(start = 0, len = 2)
    val b = segment(start = 2, len = 2)
    val c = segment(start = 4, len = 2)
    val e = segment(start = 6, len = 3)
    return "+375($a)$b-$c-$e"
}

fun normalizeNationalPhoneInput(raw: String): String {
    val digits = raw.filter { it.isDigit() }
    val national = when {
        digits.startsWith("375") && digits.length >= 3 -> digits.drop(3)
        else -> digits
    }
    return national.take(MAX_NATIONAL_DIGITS)
}

fun belarusPhoneToE164(nationalDigits: String): String {
    val d = nationalDigits.filter { it.isDigit() }.take(MAX_NATIONAL_DIGITS)
    return "+375$d"
}
