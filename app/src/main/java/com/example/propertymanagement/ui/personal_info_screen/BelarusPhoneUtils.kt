package com.example.propertymanagement.ui.personal_info_screen

internal object BelarusPhoneUtils {
    private const val COUNTRY_DIGITS = "375"
    const val E164_PREFIX = "+375"
    const val NATIONAL_DIGIT_COUNT = 9

    fun toNationalDigits(stored: String): String {
        val digits = stored.filter { it.isDigit() }
        return when {
            digits.startsWith(COUNTRY_DIGITS) && digits.length > COUNTRY_DIGITS.length ->
                digits.drop(COUNTRY_DIGITS.length).take(NATIONAL_DIGIT_COUNT)
            else -> digits.take(NATIONAL_DIGIT_COUNT)
        }
    }

    fun toE164(nationalDigits: String): String {
        val d = nationalDigits.filter { it.isDigit() }.take(NATIONAL_DIGIT_COUNT)
        return "+$COUNTRY_DIGITS$d"
    }

    fun isValidNational(nationalDigits: String): Boolean {
        return nationalDigits.filter { it.isDigit() }.length == NATIONAL_DIGIT_COUNT
    }
}
