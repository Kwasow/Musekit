package com.kwasow.musekit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kwasow.musekit.extensions.fadeComposable
import com.kwasow.musekit.ui.components.AdaptiveNavigationScaffold
import com.kwasow.musekit.ui.screens.fork.NoteForkScreen
import com.kwasow.musekit.ui.screens.metronome.MetronomeScreen
import com.kwasow.musekit.ui.screens.settings.SettingsScreen

// ====== Public composables
@Composable
fun App() {
    val navController = rememberNavController()

    AdaptiveNavigationScaffold(
        navigationItems = {
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry.value?.destination

            Routes.entries.forEach { route ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(id = route.iconId),
                            contentDescription = stringResource(id = route.titleId)
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NoteFork,
            modifier = Modifier.padding(paddingValues),
        ) {
            fadeComposable<NoteFork> { NoteForkScreen() }
            fadeComposable<Metronome> { MetronomeScreen() }
            fadeComposable<Settings> { SettingsScreen() }
        }
    }
}
