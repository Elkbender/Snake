package no.elkbender.snake.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import no.elkbender.snake.R

@Composable
fun SnakeTheme(
    content: @Composable () -> Unit,
    colorScheme: ColorScheme = darkColorScheme(
        primary = colorResource(id = R.color.monitor_dark),
        secondary = colorResource(id = R.color.monitor_dark),
        tertiary = colorResource(id = R.color.monitor_dark),
        background = colorResource(id = R.color.monitor),
        onPrimary = colorResource(id = R.color.white),
        onBackground = colorResource(id = R.color.monitor_dark)
    )
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}