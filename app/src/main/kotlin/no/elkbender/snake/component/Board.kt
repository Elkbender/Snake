package no.elkbender.snake.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import no.elkbender.snake.game.GameEngine
import no.elkbender.snake.theme.border2dp
import no.elkbender.snake.theme.corner4dp
import no.elkbender.snake.theme.padding16dp
import no.elkbender.snake.R
import kotlin.math.abs

@Composable
fun Board(gameEngine: GameEngine) {
    val state = gameEngine.gameState.collectAsState(initial = null).value ?: return
    BoxWithConstraints(Modifier.padding(padding16dp)) {
        val tileSize = maxWidth / GameEngine.BOARD_WIDTH
        Box(
            Modifier
                .size(maxWidth, maxWidth)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        when {
                            abs(x) < abs(y) && y < 0 -> if(gameEngine.move.second != 1) gameEngine.move = Pair(0, -1) /* up */
                            abs(x) > abs(y) && x < 0 -> if(gameEngine.move.first != 1) gameEngine.move = Pair(-1, 0) /* left */
                        }
                        when {
                            abs(x) > abs(y) && x > 0 -> if(gameEngine.move.first != -1) gameEngine.move = Pair(1, 0) /* right */
                            abs(x) < abs(y) && y > 0 -> if(gameEngine.move.second != -1) gameEngine.move = Pair(0, 1) /* down */
                        }
                    }
                }
                .border(border2dp, colorResource(id = R.color.monitor_dark))
        )
        Box(
            Modifier
                .offset(x = tileSize * state.food.position.first, y = tileSize * state.food.position.second)
                .size(tileSize)
                .background(
                    state.food.color, CircleShape
                )
        )
        state.snake.forEach {
            Box(
                modifier = Modifier
                    .offset(x = tileSize * it.first, y = tileSize * it.second)
                    .size(tileSize)
                    .background(
                        colorResource(id = R.color.monitor_dark), RoundedCornerShape(corner4dp)
                    )
            )
        }
    }
}