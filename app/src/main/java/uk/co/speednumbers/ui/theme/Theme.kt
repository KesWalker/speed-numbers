package com.example.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.example.ui.theme.AppTypography

private val LightThemeColors = lightColors(

	primary = md_theme_light_primary,
	onPrimary = md_theme_light_onPrimary,
	secondary = md_theme_light_secondary,
	onSecondary = md_theme_light_onSecondary,
	error = md_theme_light_error,
	onError = md_theme_light_onError,
	background = md_theme_light_background,
	onBackground = md_theme_light_onBackground,
	surface = md_theme_light_surface,
	onSurface = md_theme_light_onSurface,
	secondaryVariant = md_theme_light_background_tertiary

) 
 private val DarkThemeColors = darkColors(

	primary = md_theme_dark_primary,
	onPrimary = md_theme_dark_onPrimary,
	secondary = md_theme_dark_secondary,
	onSecondary = md_theme_dark_onSecondary,
	error = md_theme_dark_error,
	onError = md_theme_dark_onError,
	background = md_theme_dark_background,
	onBackground = md_theme_dark_onBackground,
	surface = md_theme_dark_surface,
	onSurface = md_theme_dark_onSurface,
	 secondaryVariant = md_theme_dark_background_tertiary
)
@Composable
fun SpeedNumbersTheme(
useDarkTheme: Boolean = isSystemInDarkTheme(),
content: @Composable() () -> Unit
) {
val colors = if (!useDarkTheme) {
  LightThemeColors
} else {
  DarkThemeColors
}

MaterialTheme(
  colors = colors,
  typography = AppTypography,
  content = content
)
}