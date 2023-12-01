package no.elkbender.snake.game

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import no.elkbender.snake.data.Food
import no.elkbender.snake.data.State
import no.elkbender.snake.data.GAME_LOOP_DELAY
import java.util.*

class GameEngine(
    private val context: Context,
    private val scope: CoroutineScope,
    private val onGameEnded: () -> Unit,
    private val onFoodEaten: () -> Unit
) {
    private val mutex = Mutex()
    private val _gameState = MutableStateFlow(
        State(
            food = spawnFood(),
            snake = listOf(Pair(7, 7)),
            currentDirection = SnakeDirection.Right
        )
    )
    val gameState: StateFlow<State> get() = _gameState
    private val currentDirection = mutableStateOf(SnakeDirection.Right)

    var move = Pair(1, 0)
        set(value) {
            scope.launch {
                mutex.withLock {
                    field = value
                }
            }
        }

    fun reset() {
        _gameState.update {
            it.copy(
                food = spawnFood(),
                snake = listOf(Pair(7, 7)),
                currentDirection = SnakeDirection.Right
            )
        }
        currentDirection.value = SnakeDirection.Right
        move = Pair(1, 0)
    }

    init {
        scope.launch {
            var snakeLength = 2

            while (true) {
                delay(GAME_LOOP_DELAY / (snakeLength / 2))
                _gameState.update {
                    currentDirection.value = setSnakeDirection()

                    if (wallCrash(it.snake, currentDirection.value)) {
                        snakeLength = 2
                        onGameEnded.invoke()
                    }

                    val newPosition = updatePosition(it)

                    if (newPosition == it.food.position) {
                        onFoodEaten.invoke()
                        snakeLength++
                    }

                    if (it.snake.contains(newPosition)) { // Self Crash
                        snakeLength = 2
                        onGameEnded.invoke()
                    }

                    it.copy(
                        food = if (newPosition == it.food.position) spawnFood() else it.food,
                        snake = listOf(newPosition) + it.snake.take(snakeLength - 1),
                        currentDirection = currentDirection.value,
                    )
                }
            }
        }
    }

    private fun setSnakeDirection(): Int = when (move) {
        Pair(0, -1) -> SnakeDirection.Up
        Pair(-1, 0) -> SnakeDirection.Left
        Pair(1, 0) -> SnakeDirection.Right
        Pair(0, 1) -> SnakeDirection.Down
        else -> 0
    }

    private fun wallCrash(snake: List<Pair<Int, Int>>, direction: Int): Boolean {
        val leftEnd = snake.first().first == 0 && direction == SnakeDirection.Left
        val topEnd = snake.first().second == 0 && direction == SnakeDirection.Up
        val rightEnd = snake.first().first == BOARD_WIDTH - 1 && direction == SnakeDirection.Right
        val bottomEnd = snake.first().second == BOARD_HEIGHT - 1 && direction == SnakeDirection.Down

        return leftEnd || topEnd || rightEnd || bottomEnd
    }

    private suspend fun updatePosition(state: State): Pair<Int, Int> {
        state.snake.first().let { pos ->
            mutex.withLock {
                return Pair(
                    (pos.first + move.first + BOARD_WIDTH) % BOARD_WIDTH,
                    (pos.second + move.second + BOARD_HEIGHT) % BOARD_HEIGHT
                )
            }
        }
    }

    private fun spawnFood(): Food {
        val androidColors: IntArray =
            context.resources.getIntArray(no.elkbender.snake.R.array.androidcolors)
        return Food(
            Pair(
                Random().nextInt(BOARD_WIDTH),
                Random().nextInt(BOARD_HEIGHT)
            ), Color(androidColors[Random().nextInt(androidColors.size)])
        )
    }

    companion object {
        const val BOARD_WIDTH = 32
        const val BOARD_HEIGHT = BOARD_WIDTH.div(2)
    }
}
