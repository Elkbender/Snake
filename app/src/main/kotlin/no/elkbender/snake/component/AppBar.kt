package no.elkbender.snake.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import no.elkbender.snake.theme.padding16dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    playerName: String = "",
    onBackClicked: () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit
) {
    Scaffold(topBar = {
        SmallTopAppBar(
            title = { TitleLarge(text = title) },
            actions = {
                if (playerName.isNotBlank())
                    AppButton(text = playerName, onClick = { })
            },
            navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(horizontal = padding16dp)
        )
    }) { contentPadding -> content.invoke(contentPadding) }
}