package com.example.propertymanagement.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Backgrounds
val BottomBarBackground = Color(0xFF242424)

// Accent
val PrimaryBlue = Color(0xFF2979FF)

// Text / icons
val UnselectedGray = Color(0xFF9E9E9E)

val Primary = Color(0xFF7C4DFF)
val OnPrimary = Color.White

val SurfaceVariant = Color(0xFFE0E0E0)
val OnSurfaceVariant = Color(0xFFFFFFFF)

// Цвета конкретно для FilterChip (расширяем твои)
val FilterChipSelected     = Color(0xFF5C94FF)
val FilterChipTextSelected = Color(0xFFE8F0FE)
val FilterChipIconSelected = Color(0xFFE8F0FE)

// Не выбранные состояния (Material 3 inspired)
val FilterChipUnselected = SurfaceVariant
val FilterChipTextUnselected = OnSurfaceVariant
val FilterChipIconUnselected = OnSurfaceVariant

val TopBarBackground = Color(0xFF242424)
val DividerColor       = Color(0xFF424242)


// TextField
val TextFieldBorderInactive = DividerColor
val TextFieldBorderFocused = PrimaryBlue
val TextFieldBorderError = Color.Red
val TextFieldCursor = PrimaryBlue
val TextFieldLabelColor = UnselectedGray
val TextFieldBackground = Color(0xFF2C2C2C)
val TextFieldTextColor = Color(0xFFE6E1E5)
val TextFieldHintColor = Color(0xFF9E9E9E)
val TextFieldCursorColor = Color(0xFF82B1FF)
val TextFieldBorder = Color(0xFF444444)
val TextFieldFocusedBorder = Color(0xFF82B1FF)

// Light
val TextFieldBackgroundLight = Color(0xFFF3F0F4)
val TextFieldTextColorLight = Color(0xFF1C1B1F)
val TextFieldHintColorLight = Color(0xFF9E9E9E)
val TextFieldCursorColorLight = Color(0xFF2962FF)
val TextFieldBorderLight = Color(0xFFCAC4D0)
val TextFieldFocusedBorderLight = Color(0xFF2962FF)
val CustomTextSelectionColors = TextSelectionColors(
    backgroundColor = PrimaryBlue,
    handleColor = PrimaryBlue
)

val SurfaceVariantDark = Color(0xFF4F4F4F)

// Карточка объекта — превью карты (градиент и кнопки поверх тёмного фона)
val MapPreviewGradientEnd = Color(0x80000000)
val MapFullscreenControlScrim = Color(0x73000000)
val MapControlsIconOnDark = Color.White
