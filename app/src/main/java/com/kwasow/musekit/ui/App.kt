package com.kwasow.musekit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
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
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

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

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            topLevelRoutes.forEach { route ->
                item(
                    icon = {
                        Icon(
                            painter = route.icon,
                            contentDescription = route.name,
                        )
                    },
                    label = { Text(route.name) },
                    selected =
                        currentDestination?.hierarchy?.any {
                            it.hasRoute(route.route::class)
                        } == true,
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
        },
    ) {
        Box(modifier = Modifier.safeDrawingPadding()) {
            NavHost(
                navController = navController,
                startDestination = NoteFork,
            ) {
                fadeComposable<NoteFork> { NoteForkScreen() }
                fadeComposable<Metronome> { MetronomeScreen() }
                fadeComposable<Settings> { SettingsScreen() }
            }
        }
    }
}
