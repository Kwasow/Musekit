package com.kwasow.musekit.ui.screens.worklog

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kwasow.musekit.R
import com.kwasow.musekit.ui.composition.LocalMusekitNavigation
import org.koin.compose.viewmodel.koinViewModel

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
    val navigation = LocalMusekitNavigation.current

    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.practice_history)) },
        navigationIcon = {
            IconButton(onClick = { navigation.navigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "TODO",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    )
}

@Composable
private fun MainView(paddingValues: PaddingValues) {
    val viewModel = koinViewModel<WorklogScreenViewModel>()
    val practiceSessions = viewModel.practiceSessions.observeAsState()

    val sessions = practiceSessions.value
    if (sessions == null) {
        Text("Loading...")
    } else {
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(sessions) { session ->
                Row {
                    Text(session.date.toString())
                    Text(session.length.toString())
                }
            }
        }
    }
}
