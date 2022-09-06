package no.elkbender.snake.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import no.elkbender.snake.R
import no.elkbender.snake.theme.SnakeTheme

abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnakeTheme (
                {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) { Content() }
                },
                darkColorScheme(
                    primary = colorResource(id = R.color.monitor_dark),
                    secondary = colorResource(id = R.color.monitor_dark),
                    tertiary = colorResource(id = R.color.monitor_dark),
                    background = colorResource(id = R.color.monitor),
                    onPrimary = colorResource(id = R.color.eburnean),
                    onBackground = colorResource(id = R.color.monitor_dark)
                )
            )
        }
    }

    @Composable
    abstract fun Content()
}