package com.kwasow.musekit.ui.screens.fork

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kwasow.musekit.R
import com.kwasow.musekit.ui.screens.error.ErrorScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteForkScreen() {
    // TODO: Initial state
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val pagerState = rememberPagerState(
        pageCount = { 2 },
        initialPage = viewModel.getNoteForkMode()
    )
    val coroutineScope = rememberCoroutineScope()

    val pages = listOf<Pair<Int, @Composable () -> Unit>>(
        Pair(R.string.note_fork_automatic, { NoteForkAutoScreen() }),
        Pair(R.string.note_fork_manual, { NoteForkManualScreen() }),
    )

    Column(modifier = Modifier.fillMaxSize()) {
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            pages.forEachIndexed { index, (first) ->
                Tab(
                    text = { Text(text = stringResource(id = first)) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                            viewModel.setNoteForkMode(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
        ) { page ->
            val content = pages.getOrNull(page)?.second ?: { ErrorScreen() }
            content()
        }
    }
}
