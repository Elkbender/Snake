package no.elkbender.snake.data

import androidx.compose.ui.graphics.Color

data class State(
    val food: Food,
    val snake: List<Pair<Int, Int>>,
    val currentDirection: Int
)

data class Food(val position: Pair<Int, Int>, val color: Color)