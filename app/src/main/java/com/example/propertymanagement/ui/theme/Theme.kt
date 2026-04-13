package com.example.propertymanagement.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.propertymanagement.domain.model.ThemeType

private val DarkColorScheme = darkColorScheme(

    primary = Color(0xFF82B1FF),
    onPrimary = Color(0xFF002E6E),
    primaryContainer = Color(0xFF0A3E91),
    onPrimaryContainer = Color(0xFFD6E3FF),

    secondary = Color(0xFFB39DFF),
    onSecondary = Color(0xFF2A1A6A),
    secondaryContainer = Color(0xFF3E2E8F),
    onSecondaryContainer = Color(0xFFE8DDFF),

    tertiary = Color(0xFFFFB1C5),
    onTertiary = Color(0xFF5A1125),
    tertiaryContainer = Color(0xFF7A2A3D),
    onTertiaryContainer = Color(0xFFFFD9E2),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE6E1E5),

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE6E1E5),

    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFCAC4D0),

    surfaceTint = Color(0xFF82B1FF),

    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF1E1E1E),
    inversePrimary = Color(0xFF2979FF),

    outline = Color(0xFF8A8A8A),
    outlineVariant = Color(0xFF444444),

    scrim = Color.Black.copy(alpha = 0.6f),

    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFFFDAD6)
)

private val LightColorScheme = lightColorScheme(

    primary = Color(0xFF2962FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E3FF),
    onPrimaryContainer = Color(0xFF001B3F),

    secondary = Color(0xFF6A4EFF),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DDFF),
    onSecondaryContainer = Color(0xFF22005D),

    tertiary = Color(0xFFB3265E),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD9E2),
    onTertiaryContainer = Color(0xFF3F001A),

    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1C1B1F),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),

    surfaceVariant = Color(0xFFF3F0F4),
    onSurfaceVariant = Color(0xFF49454F),

    surfaceTint = Color(0xFF2962FF),

    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFF82B1FF),

    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),

    scrim = Color.Black.copy(alpha = 0.3f),

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFCD8DF),
    onErrorContainer = Color(0xFF370617)
)

@Composable
fun PropertyManagementTheme(
    themeType: ThemeType,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val darkTheme = when (themeType) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}