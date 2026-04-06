package com.kwasow.musekit.ui.screens.worklog

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.HourglassFull
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.kwasow.musekit.R
import com.kwasow.musekit.room.PracticeSession
import com.kwasow.musekit.ui.components.ListDivider
import com.kwasow.musekit.ui.components.ListEntry
import com.kwasow.musekit.ui.components.ListSection
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
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription =
                        stringResource(
                            id = R.string.contentDescription_back_arrow,
                        ),
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
                items(byMonth.keys.sortedByDescending { (year, month) -> year * 12 + month }) {
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

    ListSection(title = "$monthName $year") {
        sessions.sortedByDescending(PracticeSession::date).forEachIndexed { index, session ->
            PracticeEntry(session = session)

            if (index < sessions.lastIndex) {
                ListDivider()
            }
        }
    }
}

@Composable
private fun PracticeEntry(session: PracticeSession) {
    val iconLittlePractice = rememberVectorPainter(Icons.Outlined.HourglassEmpty)
    val iconSomePractice = rememberVectorPainter(Icons.Outlined.HourglassBottom)
    val iconFullPractice = rememberVectorPainter(Icons.Outlined.HourglassFull)

    val (icon, percent) =
        if (session.length < 30 * 60) {
            Pair(iconLittlePractice, 0)
        } else if (session.length < 60 * 60) {
            Pair(iconSomePractice, 50)
        } else {
            Pair(iconFullPractice, 100)
        }

    ListEntry(
        icon = icon,
        iconDescription = stringResource(id = R.string.contentDescription_hourglass, percent),
        header = DateUtils.formatElapsedTime(session.length),
        description = session.date.toString(),
        onClick = null,
    )
}
