package no.elkbender.snake.activity

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import no.elkbender.snake.data.GameCache
import no.elkbender.snake.data.HighScore
import no.elkbender.snake.data.TOP_10
import no.elkbender.snake.game.GameEngine
import no.elkbender.snake.screen.EndScreen
import no.elkbender.snake.screen.GameScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.elkbender.snake.R

class GameActivity : BaseActivity() {
    private lateinit var dataStore: GameCache
    private val isPlaying = mutableStateOf(true)
    private var score = mutableStateOf(0)
    private lateinit var scope: CoroutineScope
    private lateinit var playerName: String
    private lateinit var highScores: List<HighScore>
    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameEngine = GameEngine(
            context = applicationContext,
            scope = lifecycleScope,
            onGameEnded = {
                if (isPlaying.value) {
                    isPlaying.value = false
                    scope.launch { dataStore.saveHighScore(highScores) }
                }
            },
            onFoodEaten = { score.value++ }
        )
    }

    @Composable
    override fun Content() {
        scope = rememberCoroutineScope()
        dataStore = GameCache(applicationContext)
        playerName = dataStore.getPlayerName.collectAsState(initial = stringResource(id = R.string.default_player_name)).value
        highScores = dataStore.getHighScores.collectAsState(initial = listOf()).value.plus(
            HighScore(playerName, score.value)
        ).sortedByDescending { it.score }.take(TOP_10)
        Column {
            if (isPlaying.value) {
                GameScreen(gameEngine, score.value, playerName)
            } else {
                EndScreen(score.value) {
                    score.value = 0
                    gameEngine.reset()
                    isPlaying.value = true
                }
            }
        }
    }
}