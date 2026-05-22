package com.example.propertymanagement.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.propertymanagement.domain.model.ThemeType

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5BD4CB),
    onPrimary = Color(0xFF003732),
    primaryContainer = Color(0xFF005048),
    onPrimaryContainer = Color(0xFF8BF2E8),

    secondary = Color(0xFFB1CAC7),
    onSecondary = Color(0xFF1C3532),
    secondaryContainer = Color(0xFF334B48),
    onSecondaryContainer = Color(0xFFCDE8E4),

    tertiary = Color(0xFFB8C9A8),
    onTertiary = Color(0xFF243623),
    tertiaryContainer = Color(0xFF3A4D38),
    onTertiaryContainer = Color(0xFFD4E8C9),

    background = Color(0xFF0E1214),
    onBackground = Color(0xFFE1E6E7),

    surface = Color(0xFF12181A),
    onSurface = Color(0xFFE1E6E7),

    surfaceVariant = Color(0xFF2A3235),
    onSurfaceVariant = Color(0xFFBFC9CA),

    surfaceTint = Color(0xFF5BD4CB),

    inverseSurface = Color(0xFFE1E6E7),
    inverseOnSurface = Color(0xFF12181A),
    inversePrimary = Color(0xFF0D5C56),

    outline = Color(0xFF899394),
    outlineVariant = Color(0xFF3F484A),

    scrim = Color.Black.copy(alpha = 0.62f),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0D5C56),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA8F2EA),
    onPrimaryContainer = Color(0xFF00201D),

    secondary = Color(0xFF3E5B57),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBFE8E1),
    onSecondaryContainer = Color(0xFF08201F),

    tertiary = Color(0xFF4C6338),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD4EABC),
    onTertiaryContainer = Color(0xFF102000),

    background = Color(0xFFF5F7F7),
    onBackground = Color(0xFF1A1C1C),

    surface = Color(0xFFFCFEFE),
    onSurface = Color(0xFF1A1C1C),

    surfaceVariant = Color(0xFFDAE5E4),
    onSurfaceVariant = Color(0xFF3F4948),

    surfaceTint = Color(0xFF0D5C56),

    inverseSurface = Color(0xFF2A3235),
    inverseOnSurface = Color(0xFFE8F5F5),
    inversePrimary = Color(0xFF5BD4CB),

    outline = Color(0xFF6F7978),
    outlineVariant = Color(0xFFBFC9CA),

    scrim = Color.Black.copy(alpha = 0.32f),

    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFCD8DF),
    onErrorContainer = Color(0xFF370617),
)

@Composable
fun PropertyManagementTheme(
    themeType: ThemeType,
    content: @Composable () -> Unit,
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
        shapes = AppShapes,
        content = content,
    )
}
