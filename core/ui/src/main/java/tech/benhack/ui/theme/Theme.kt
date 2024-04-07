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
    tertiary = Gray500,
    onSecondary = Color.White,
    error = DeepOrange900,
    onError = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Gray800,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerLow = Gray100,
    inverseSurface =  Gray100
)

private val darkColorScheme = darkColorScheme(
    primary = Yellow400,
    onPrimary = Color.Black,
    secondary = Gray500,
    tertiary = Gray100,
    onSecondary = Color.White,
    error = DeepOrange900,
    onError = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Gray800,
    onSurface = Color.White,
    surfaceContainer = Gray800,
    surfaceContainerHigh = DarkGrey,
    surfaceContainerLow = Gray500,
    inverseSurface =  Color.Black,
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