package no.elkbender.snake.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Key(modifier: Modifier = Modifier, label: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = modifier
            .padding(3.dp)
            .clip(shape)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        KeyboardText(text = label)
    }
}

@Composable
fun KeyRow(keys: List<String>, callback: (text: String) -> Any) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.background)) {
        keys.forEach {
            Key(modifier = Modifier.weight(1f), label = it, onClick = { callback })
        }
    }
}
