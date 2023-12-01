package no.elkbender.snake.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import no.elkbender.snake.R
import no.elkbender.snake.data.GameCache
import no.elkbender.snake.component.AppBar
import no.elkbender.snake.component.AppButton
import no.elkbender.snake.component.DisplayLarge
import no.elkbender.snake.component.TitleLarge
import no.elkbender.snake.theme.border2dp
import no.elkbender.snake.theme.padding16dp
import no.elkbender.snake.theme.padding64dp
import no.elkbender.snake.theme.width248dp
import kotlin.reflect.KSuspendFunction1

@Preview
@Composable
fun SettingsScreen(navController: NavHostController? = null) {
    val context = LocalContext.current
    val dataStore = GameCache(context)
    val currentDifficulty = dataStore.getDifficulty.collectAsState(initial = 1).value
    val currentWrapAroundMode = dataStore.getDifficulty.collectAsState(initial = 0).value
    val scrollState = rememberScrollState()

    AppBar(
        title = stringResource(R.string.title_settings),
        onBackClicked = { navController?.popBackStack() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = padding16dp,
                    start = padding16dp,
                    end = padding16dp
                )
                .border(width = border2dp, color = MaterialTheme.colorScheme.onBackground)
                .verticalScroll(
                    enabled = true,
                    state = scrollState
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlayerName(navController)
            HorizontalSliderModule(
                title = stringResource(id = R.string.difficulty_title), list = listOf(
                    stringResource(id = R.string.difficulty_easy),
                    stringResource(id = R.string.difficulty_medium),
                    stringResource(id = R.string.difficulty_hard)
                ), pageCount = 3, initialPage = currentDifficulty, function = dataStore::saveDifficulty
            )
            HorizontalSliderModule(
                title = stringResource(id = R.string.wrap_around), list = listOf(
                    stringResource(id = R.string.off),
                    stringResource(id = R.string.on)
                ), pageCount = 2, initialPage = currentWrapAroundMode, function = dataStore::saveWrapAroundMode
            )
            HorizontalSliderModule(
                title = stringResource(id = R.string.theme),
                list = stringArrayResource(id = R.array.themes).toList(),
                pageCount = 3,
                initialPage = currentWrapAroundMode,
                function = dataStore::saveWrapAroundMode
            )
        }
    }
}

@Composable
fun PlayerName(navController: NavHostController? = null) {
    val dataStore = GameCache(LocalContext.current)
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    TitleLarge(
        modifier = Modifier.padding(
            top = padding64dp,
            bottom = padding16dp,
            start = padding16dp,
            end = padding16dp
        ),
        text = stringResource(id = R.string.player_name),
        decoration = TextDecoration.Underline
    )
    TextField(
        value = textFieldValue,
        onValueChange = { textFieldValue = it },
        colors = TextFieldDefaults.colors(),
        singleLine = true,
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .padding(horizontal = padding64dp)
            .border(width = border2dp, color = MaterialTheme.colorScheme.onBackground)
    )
    AppButton(
        text = stringResource(R.string.save), modifier = Modifier
            .width(width248dp)
            .padding(padding16dp)
    ) {
        scope.launch {
            dataStore.savePlayerName(textFieldValue.text.trim())
            Toast.makeText(context, R.string.player_name_updated, Toast.LENGTH_SHORT).show()
            navController?.popBackStack()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalSliderModule(
    title: String,
    list: List<String>,
    pageCount: Int,
    initialPage: Int,
    function: KSuspendFunction1<Int, Unit>
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = initialPage)

    TitleLarge(
        modifier = Modifier.padding(top = padding64dp),
        text = title,
        decoration = TextDecoration.Underline
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(
                start = padding16dp,
                end = padding16dp
            )
    ) {
        Box(
            modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = {
                    scope.launch {
                        if (pagerState.currentPage >= 1) {
                            pagerState.scrollToPage(pagerState.currentPage - 1)
                        }
                    }
                })
                .background(MaterialTheme.colorScheme.background)
        ) {
            DisplayLarge(text = "<")
        }
        HorizontalPager(
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .fillMaxWidth(0.5f),
            count = pageCount,
            state = pagerState
        ) { page ->
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    function(page)
                }
            }

            TitleLarge(
                text = list[page],
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = {
                    scope.launch {
                        if (pagerState.currentPage <= pageCount - 1) {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        }
                    }
                })
                .background(MaterialTheme.colorScheme.background)
        ) {
            DisplayLarge(text = ">")
        }
    }
}