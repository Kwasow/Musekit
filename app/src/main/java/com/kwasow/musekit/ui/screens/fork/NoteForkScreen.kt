package com.kwasow.musekit.ui.screens.fork

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.ui.components.PilledTabItem
import com.kwasow.musekit.ui.components.PilledTabRow
import com.kwasow.musekit.ui.screens.error.ErrorScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun NoteForkScreen() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val noteForkMode = runBlocking { viewModel.noteForkMode.first() }
    val pagerState =
        rememberPagerState(
            pageCount = { 2 },
            initialPage = noteForkMode,
        )

    val pages =
        listOf<Pair<Int, @Composable () -> Unit>>(
            Pair(R.string.note_fork_automatic, { NoteForkAutoScreen() }),
            Pair(R.string.note_fork_manual, { NoteForkManualScreen() }),
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
    ) {
        Navigation(
            pages = pages,
            pagerState = pagerState,
        )

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            contentPadding = PaddingValues(vertical = 12.dp),
        ) { page ->
            val content = pages.getOrNull(page)?.second ?: { ErrorScreen() }
            content()
        }
    }
}

// ====== Private composables
@Composable
private fun Navigation(
    pages: List<Pair<Int, @Composable (() -> Unit)>>,
    pagerState: PagerState,
) {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val coroutineScope = rememberCoroutineScope()

    PilledTabRow(modifier = Modifier.padding(horizontal = 24.dp)) {
        pages.forEachIndexed { index, (first) ->
            val selected = pagerState.currentPage == index

            PilledTabItem(
                text = stringResource(id = first),
                isSelected = selected,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                        viewModel.setNoteForkMode(index)
                    }
                },
            )
        }
    }
}
