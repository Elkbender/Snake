package no.elkbender.snake.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import no.elkbender.snake.game.GameEngine
import no.elkbender.snake.game.SnakeDirection
import no.elkbender.snake.activity.GameActivity
import no.elkbender.snake.component.AppBar
import no.elkbender.snake.component.Board
import no.elkbender.snake.component.Controller
import no.elkbender.snake.R

@Composable
fun GameScreen(gameEngine: GameEngine, score: Int, playerName: String) {
    val state = gameEngine.gameState.collectAsState(initial = null)
    val activity = LocalContext.current as GameActivity
    AppBar(
        title = stringResource(id = R.string.your_score, score),
        playerName = playerName,
        onBackClicked = { activity.finish() }) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.value?.let { Board(gameEngine) }
            Controller {
                when (it) {
                    SnakeDirection.Up -> gameEngine.move = Pair(0, -1)
                    SnakeDirection.Left -> gameEngine.move = Pair(-1, 0)
                    SnakeDirection.Right -> gameEngine.move = Pair(1, 0)
                    SnakeDirection.Down -> gameEngine.move = Pair(0, 1)
                }
            }
        }
    }
}