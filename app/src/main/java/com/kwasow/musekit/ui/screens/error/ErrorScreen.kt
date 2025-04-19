package com.kwasow.musekit.ui.screens.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import kotlin.system.exitProcess

// ====== Public composables
@Composable
fun ErrorScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_ghost),
            contentDescription = stringResource(id = R.string.contentDescription_ghost_icon),
            modifier = Modifier.size(240.dp),
        )

        Text(
            text = stringResource(id = R.string.whoopsie),
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Button(
            onClick = { exitProcess(0) },
        ) {
            Text(text = stringResource(id = R.string.close_app))
        }
    }
}
