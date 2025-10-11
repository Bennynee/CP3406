package com.readingjournal.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.readingjournal.app.ui.theme.DarkBackground
import com.readingjournal.app.ui.theme.DarkPrimary
import com.readingjournal.app.ui.theme.DarkSecondary
import com.readingjournal.app.ui.theme.DarkSurface
import com.readingjournal.app.ui.theme.DarkSurfaceVariant
import com.readingjournal.app.ui.theme.DarkTertiary
import com.readingjournal.app.ui.theme.LightBackground
import com.readingjournal.app.ui.theme.LightPrimary
import com.readingjournal.app.ui.theme.LightSecondary
import com.readingjournal.app.ui.theme.LightSurface
import com.readingjournal.app.ui.theme.LightSurfaceVariant
import com.readingjournal.app.ui.theme.LightTertiary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primaryContainer = Color(0xFF1E3A5F),
    secondaryContainer = Color(0xFF3D2A4F),
    tertiaryContainer = Color(0xFF5A1A2F),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onPrimaryContainer = Color(0xFFB3D4FF),
    onSecondaryContainer = Color(0xFFE8D4FF),
    onTertiaryContainer = Color(0xFFFFB3D1)
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    primaryContainer = Color(0xFFE3F2FD),
    secondaryContainer = Color(0xFFF3E5F5),
    tertiaryContainer = Color(0xFFFFE0E6),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onPrimaryContainer = Color(0xFF0D47A1),
    onSecondaryContainer = Color(0xFF4A148C),
    onTertiaryContainer = Color(0xFF880E4F)
)

@Composable
fun ReadingJournalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
