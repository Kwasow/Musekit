package com.kwasow.musekit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kwasow.musekit.R
import com.kwasow.musekit.ui.screens.fork.NoteForkScreen
import com.kwasow.musekit.ui.screens.metronome.MetronomeScreen
import com.kwasow.musekit.ui.screens.settings.SettingsScreen

// ====== Public composables
@Composable
fun App() {
    val navController = rememberNavController()

    val topLevelRoutes =
        listOf(
            TopLevelRoute(
                stringResource(id = R.string.tuning),
                NoteFork,
                painterResource(id = R.drawable.ic_note_fork),
            ),
            TopLevelRoute(
                stringResource(id = R.string.metronome),
                Metronome,
                painterResource(id = R.drawable.ic_metronome),
            ),
            TopLevelRoute(
                stringResource(id = R.string.settings),
                Settings,
                painterResource(id = R.drawable.ic_settings),
            ),
        )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry.value?.destination

                topLevelRoutes.forEach { route ->
                    NavigationBarItem(
                        icon = { Icon(route.icon, contentDescription = route.name) },
                        label = { Text(route.name) },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(route.route::class) } == true,
                        onClick = {
                            navController.navigate(route.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NoteFork,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable<NoteFork> { NoteForkScreen() }
            composable<Metronome> { MetronomeScreen() }
            composable<Settings> { SettingsScreen() }
        }
    }
}
