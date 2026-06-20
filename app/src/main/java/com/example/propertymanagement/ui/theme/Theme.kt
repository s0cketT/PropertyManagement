package com.example.propertymanagement.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.propertymanagement.domain.model.ThemeType

val LocalAppDarkTheme = staticCompositionLocalOf { false }

@Composable
fun resolveAppDarkTheme(themeType: ThemeType): Boolean {
    return when (themeType) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }
}

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

    background = Color(0xFFEAE3D8),
    onBackground = Color(0xFF1F1C18),

    surface = Color(0xFFF3EFE7),
    onSurface = Color(0xFF1F1C18),

    surfaceVariant = Color(0xFFDAD2C4),
    onSurfaceVariant = Color(0xFF4A4540),

    surfaceDim = Color(0xFFE2DBD0),
    surfaceBright = Color(0xFFF8F4ED),
    surfaceContainerLowest = Color(0xFFDFDBD3),
    surfaceContainerLow = Color(0xFFEAE3D8),
    surfaceContainer = Color(0xFFEFEBE3),
    surfaceContainerHigh = Color(0xFFF3EFE7),
    surfaceContainerHighest = Color(0xFFF8F4ED),

    surfaceTint = Color(0xFF0D5C56),

    inverseSurface = Color(0xFF2A3235),
    inverseOnSurface = Color(0xFFF2EDE5),
    inversePrimary = Color(0xFF5BD4CB),

    outline = Color(0xFF7D756A),
    outlineVariant = Color(0xFFC8BFB2),

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
    val darkTheme = resolveAppDarkTheme(themeType)

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalAppDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content,
        )
    }
}
