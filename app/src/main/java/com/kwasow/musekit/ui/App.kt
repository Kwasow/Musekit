package com.kwasow.musekit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kwasow.musekit.R
import com.kwasow.musekit.extensions.fadeComposable
import com.kwasow.musekit.ui.screens.fork.NoteForkScreen
import com.kwasow.musekit.ui.screens.metronome.MetronomeScreen
import com.kwasow.musekit.ui.screens.settings.SettingsScreen

// ====== Public composables
@Composable
fun App() {
    val navController = rememberNavController()
    var currentDestination by rememberSaveable { mutableStateOf(TopLevelRoutes.NoteFork) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelRoutes.entries.forEach { route ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(id = route.icon),
                            contentDescription = stringResource(route.label),
                        )
                    },
                    label = { Text(stringResource(route.label)) },
                    selected = currentDestination == route,
                    onClick = { currentDestination = route },
                )
            }
        }
    ) {
        // TODO: Fade animations
        Box(modifier = Modifier.safeDrawingPadding()) {
            when (currentDestination) {
                TopLevelRoutes.NoteFork -> NoteForkScreen()
                TopLevelRoutes.Metronome -> MetronomeScreen()
                TopLevelRoutes.Settings -> SettingsScreen()
            }
        }
    }
}
