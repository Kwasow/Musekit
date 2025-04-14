package com.kwasow.musekit.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.ComposeErrorFragmentBinding
import com.kwasow.musekit.databinding.ComposeMetronomeFragmentBinding
import com.kwasow.musekit.databinding.ComposeNoteForkFragmentBinding
import com.kwasow.musekit.databinding.ComposeSettingsFragmentBinding
import kotlinx.coroutines.launch

// ====== Public composables
@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })

    val items = listOf(
        Pair(R.string.tuning, R.drawable.ic_note_fork),
        Pair(R.string.metronome, R.drawable.ic_metronome),
        Pair(R.string.settings, R.drawable.ic_settings)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, (nameId, iconId) ->
                    val name = stringResource(id = nameId)

                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(id = iconId),
                                contentDescription = name
                            )
                        },
                        label = { Text(name) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> NoteForkPage()
                1 -> MetronomePage()
                2 -> SettingsPage()
                else -> ErrorPage()
            }
        }
    }
}

// ====== Private composables
@Composable
private fun NoteForkPage() {
    AndroidViewBinding(ComposeNoteForkFragmentBinding::inflate)
}

@Composable
private fun MetronomePage() {
    AndroidViewBinding(ComposeMetronomeFragmentBinding::inflate)
}

@Composable
private fun SettingsPage() {
    AndroidViewBinding(ComposeSettingsFragmentBinding::inflate)
}

@Composable
private fun ErrorPage() {
    AndroidViewBinding(ComposeErrorFragmentBinding::inflate)
}
