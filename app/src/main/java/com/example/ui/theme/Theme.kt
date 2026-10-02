package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RachaDarkColorScheme = darkColorScheme(
    primary = NeonOrangePrimary,
    onPrimary = TextWhitePrimary,
    primaryContainer = NeonDarkSurfaceCard,
    onPrimaryContainer = NeonOrangeLight,
    secondary = NeonCyan,
    onSecondary = NeonDarkBg,
    secondaryContainer = NeonDarkSurfaceBorder,
    onSecondaryContainer = NeonCyan,
    tertiary = NeonLime,
    onTertiary = NeonDarkBg,
    background = NeonDarkBg,
    onBackground = TextWhitePrimary,
    surface = NeonDarkSurface,
    onSurface = TextWhitePrimary,
    surfaceVariant = NeonDarkSurfaceCard,
    onSurfaceVariant = TextMutedSecondary
)

@Composable
fun RachaTheme(
    content: @Composable () -> Unit
) {
    // RACHA enforces dark mode with neon accents as per app design requirements
    MaterialTheme(
        colorScheme = RachaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
