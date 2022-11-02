package com.example.compose

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val md_theme_light_primary = Color(0xFF005ac2)
val md_theme_light_onPrimary = Color(0xFFffffff)
val md_theme_light_primaryContainer = Color(0xFFd6e2ff)
val md_theme_light_onPrimaryContainer = Color(0xFF001a43)
val md_theme_light_secondary = Color(0xFF565e71)
val md_theme_light_onSecondary = Color(0xFFffffff)
val md_theme_light_secondaryContainer = Color(0xFFdae2f9)
val md_theme_light_onSecondaryContainer = Color(0xFF131b2c)
val md_theme_light_tertiary = Color(0xFF705573)
val md_theme_light_onTertiary = Color(0xFFffffff)
val md_theme_light_tertiaryContainer = Color(0xFFfad7fb)
val md_theme_light_onTertiaryContainer = Color(0xFF29132d)
val md_theme_light_error = Color(0xFFba1b1b)
val md_theme_light_errorContainer = Color(0xFFffdad4)
val md_theme_light_onError = Color(0xFFffffff)
val md_theme_light_onErrorContainer = Color(0xFF410001)
val md_theme_light_background = Color(0xFFf5f5f5)
val md_theme_light_background_tertiary = Color(0xFFe5e5e5)
val md_theme_light_onBackground = Color(0xFF1b1b1d)
val md_theme_light_surface = Color(0xFFfdfbff)
val md_theme_light_onSurface = Color(0xFF1b1b1d)
val md_theme_light_surfaceVariant = Color(0xFFe1e2ec)
val md_theme_light_onSurfaceVariant = Color(0xFF44474f)
val md_theme_light_outline = Color(0xFF74777f)
val md_theme_light_inverseOnSurface = Color(0xFFf2f0f4)
val md_theme_light_inverseSurface = Color(0xFF2f3033)
val md_theme_light_inversePrimary = Color(0xFFabc7ff)
val md_theme_light_shadow = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFabc7ff)
val md_theme_dark_onPrimary = Color(0xFF002e6a)
val md_theme_dark_primaryContainer = Color(0xFF004495)
val md_theme_dark_onPrimaryContainer = Color(0xFFd6e2ff)
val md_theme_dark_secondary = Color(0xFFbec6dc)
val md_theme_dark_onSecondary = Color(0xFF283041)
val md_theme_dark_secondaryContainer = Color(0xFF3f4759)
val md_theme_dark_onSecondaryContainer = Color(0xFFdae2f9)
val md_theme_dark_tertiary = Color(0xFFdebcdf)
val md_theme_dark_onTertiary = Color(0xFF402844)
val md_theme_dark_tertiaryContainer = Color(0xFF583e5b)
val md_theme_dark_onTertiaryContainer = Color(0xFFfad7fb)
val md_theme_dark_error = Color(0xFFffb4a9)
val md_theme_dark_errorContainer = Color(0xFF930006)
val md_theme_dark_onError = Color(0xFF680003)
val md_theme_dark_onErrorContainer = Color(0xFFffdad4)
val md_theme_dark_background = Color(0xFF1b1b1d)
val md_theme_dark_background_tertiary = Color(0xFF2b2b2d)
val md_theme_dark_onBackground = Color(0xFFe4e2e6)
val md_theme_dark_surface = Color(0xFF1b1b1d)
val md_theme_dark_onSurface = Color(0xFFe4e2e6)
val md_theme_dark_surfaceVariant = Color(0xFF44474f)
val md_theme_dark_onSurfaceVariant = Color(0xFFc4c6d0)
val md_theme_dark_outline = Color(0xFF8e9099)
val md_theme_dark_inverseOnSurface = Color(0xFF1b1b1d)
val md_theme_dark_inverseSurface = Color(0xFFe4e2e6)
val md_theme_dark_inversePrimary = Color(0xFF005ac2)
val md_theme_dark_shadow = Color(0xFF000000)

val seed = Color(0xFF0079fa)
val error = Color(0xFFba1b1b)

val Colors.correctGreen: Color
    get() = if (isLight) Color(0xFF38D600) else Color(0xFF7BDD59)

val Colors.nearlyOrange: Color
    get() = if (isLight) Color(0xFFFF7A00) else Color(0xFFFFB775)

val Colors.incorrectRed: Color
    get() = if (isLight) Color(0xFFFF0000) else Color(0xFFFFB4A9)

val Colors.onCorrectGreen: Color
    get() = if (isLight) Color(0xFFFFFFFF) else Color(0xFF053900)

val Colors.onNearlyOrange: Color
    get() = if (isLight) Color(0xFFFFFFFF) else Color(0xFF4D2700)

val Colors.onIncorrectRed: Color
    get() = if (isLight) Color(0xFFFFFFFF) else Color(0xFF69000C)
