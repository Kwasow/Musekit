package com.kwasow.musekit.ui.screens.worklog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kwasow.musekit.R
import com.kwasow.musekit.room.PracticeSession
import com.kwasow.musekit.ui.components.SettingsSection
import com.kwasow.musekit.ui.composition.LocalMusekitNavigation
import org.koin.compose.viewmodel.koinViewModel
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

// ====== Public composables
@Composable
fun WorklogScreen() {
    Scaffold(
        topBar = { TopBar() },
    ) { paddingValues ->
        MainView(paddingValues = paddingValues)
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    val (_, navigateBack) = LocalMusekitNavigation.current

    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.practice_history)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "TODO",
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
    )
}

@Composable
private fun MainView(paddingValues: PaddingValues) {
    val viewModel = koinViewModel<WorklogScreenViewModel>()
    val practiceSessions = viewModel.practiceSessions.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getPracticeSessions()
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
    ) {
        val sessions = practiceSessions.value

        if (sessions == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            val byMonth =
                sessions.groupBy { (date) ->
                    Pair(date.year, date.monthValue)
                }

            LazyColumn {
                items(byMonth.keys.sortedBy { (year, month) -> year * 12 + month }) {
                    PracticeEntriesSection(
                        month = it.second,
                        year = it.first,
                        sessions = byMonth[it] ?: emptyList(),
                    )
                }
            }
        }
    }
}

@Composable
private fun PracticeEntriesSection(
    month: Int,
    year: Int,
    sessions: List<PracticeSession>,
) {
    val monthName = Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())

    SettingsSection(title = "$monthName $year") {
        sessions.forEach {
            PracticeEntry(session = it)
        }
    }
}

@Composable
private fun PracticeEntry(session: PracticeSession) {
    Text(
        "${session.date} // ${session.length}",
    )
}
