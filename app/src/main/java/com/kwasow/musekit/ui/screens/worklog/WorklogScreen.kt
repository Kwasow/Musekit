package com.kwasow.musekit.ui.screens.worklog

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import com.kwasow.musekit.R
import com.kwasow.musekit.room.PracticeSession
import com.kwasow.musekit.ui.components.ListEntry
import com.kwasow.musekit.ui.components.ListSection
import com.kwasow.musekit.ui.composition.LocalMusekitNavigation
import com.kwasow.musekit.utils.ScreenUtils
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

// ====== Public composables
@Composable
fun WorklogScreen() {
    val viewModel = koinViewModel<WorklogScreenViewModel>()
    val practiceSessions = viewModel.practiceSessions.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getPracticeSessions()
    }

    WorklogScreen(practiceSessions = practiceSessions.value)
}

// ====== Private composables
@Composable
private fun WorklogScreen(practiceSessions: List<PracticeSession>?) {
    Scaffold(
        topBar = { TopBar() },
    ) { paddingValues ->
        MainView(
            paddingValues = paddingValues,
            practiceSessions = practiceSessions,
        )
    }
}

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
private fun MainView(
    paddingValues: PaddingValues,
    practiceSessions: List<PracticeSession>?,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
    ) {
        if (practiceSessions == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            val byMonth =
                practiceSessions.groupBy { (date) ->
                    Pair(date.year, date.monthValue)
                }

            Column {
                byMonth.keys.sortedByDescending { (year, month) -> year * 12 + month }.forEach {
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

    ListSection(
        title = "$monthName $year",
        items = sessions.sortedByDescending(PracticeSession::date),
        columns = if (ScreenUtils.isWide()) GridCells.Fixed(2) else GridCells.Fixed(1),
    ) { _, item ->
        PracticeEntry(session = item)
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

// ====== Previews
@Composable
@Preview(showSystemUi = true, device = PHONE)
private fun PhonePortraitPreview() {
    val practiceSessions =
        listOf(
            PracticeSession(
                date = LocalDate.of(2026, 4, 6),
                length = 768,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 4, 5),
                length = 1890,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 4, 3),
                length = 544,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 3, 22),
                length = 3865,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 3, 21),
                length = 153,
            ),
        )

    WorklogScreen(practiceSessions = practiceSessions)
}

@Composable
@Preview(showSystemUi = true, device = "$PHONE,orientation=landscape")
private fun PhoneLandscapePreview() {
    val practiceSessions =
        listOf(
            PracticeSession(
                date = LocalDate.of(2026, 4, 6),
                length = 768,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 4, 5),
                length = 1890,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 4, 3),
                length = 544,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 3, 22),
                length = 3865,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 3, 21),
                length = 153,
            ),
        )

    WorklogScreen(practiceSessions = practiceSessions)
}

@Composable
@Preview(showSystemUi = true, device = "$TABLET,orientation=portrait")
private fun TabletPortraitPreview() {
    val practiceSessions =
        listOf(
            PracticeSession(
                date = LocalDate.of(2026, 4, 6),
                length = 768,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 4, 5),
                length = 1890,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 4, 3),
                length = 544,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 3, 22),
                length = 3865,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 3, 21),
                length = 153,
            ),
        )

    WorklogScreen(practiceSessions = practiceSessions)
}

@Composable
@Preview(showSystemUi = true, device = TABLET)
private fun TabletLandscapePreview() {
    val practiceSessions =
        listOf(
            PracticeSession(
                date = LocalDate.of(2026, 4, 6),
                length = 768,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 4, 5),
                length = 1890,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 4, 3),
                length = 544,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 3, 22),
                length = 3865,
            ),
            PracticeSession(
                date = LocalDate.of(2026, 3, 21),
                length = 153,
            ),
        )

    WorklogScreen(practiceSessions = practiceSessions)
}
