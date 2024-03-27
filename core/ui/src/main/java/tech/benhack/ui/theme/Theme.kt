package tech.benhack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightColorScheme = lightColorScheme(
    primary = Yellow400,
    onPrimary = Color.Black,
    secondary = Gray500,
    onSecondary = Color.White,
    error = DeepOrange900,
    onError = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Gray500,
    surfaceContainer = Color.White
)

private val darkColorScheme = darkColorScheme(
    primary = Yellow400,
    onPrimary = Color.Black,
    secondary = Gray500,
    onSecondary = Color.White,
    error = DeepOrange900,
    onError = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Gray100,
    onSurface = Gray500,
    surfaceContainer = Gray800
)

object TrailerxTheme {
    var colorScheme = lightColorScheme
}

@Composable
fun TrailerxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){
    TrailerxTheme.colorScheme =
        if (darkTheme) {
            darkColorScheme
        } else {
            lightColorScheme
        }
    MaterialTheme(
        colorScheme = TrailerxTheme.colorScheme,
        typography = trailerxTypography,
        content = content,
        shapes = trailerxShapes
    )
}