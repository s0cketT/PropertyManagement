package com.example.propertymanagement.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TextMedium,
        lineHeight = TypeBodyLineHeight,
        letterSpacing = TypeLetterSpacingTight
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TypeTitleFontSize,
        lineHeight = TypeTitleLineHeight,
        letterSpacing = SpZero
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = TypeLabelFontSize,
        lineHeight = TypeLabelLineHeight,
        letterSpacing = TypeLetterSpacingTight
    )
    */
)