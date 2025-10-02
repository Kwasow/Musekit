package com.kwasow.musekit.ui.screens.worklog

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kwasow.musekit.R
import com.kwasow.musekit.ui.composition.LocalMusekitNavigation

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
    )
}

@Composable
private fun MainView(paddingValues: PaddingValues) {
    Text("Test", modifier = Modifier.padding(paddingValues))
}
