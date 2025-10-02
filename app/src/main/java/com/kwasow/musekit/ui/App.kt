package com.kwasow.musekit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kwasow.musekit.R
import com.kwasow.musekit.extensions.fadeComposable
import com.kwasow.musekit.ui.composition.LocalMusekitNavigation
import com.kwasow.musekit.ui.composition.MusekitNavigation
import com.kwasow.musekit.ui.screens.fork.NoteForkScreen
import com.kwasow.musekit.ui.screens.metronome.MetronomeScreen
import com.kwasow.musekit.ui.screens.settings.SettingsScreen
import com.kwasow.musekit.ui.screens.worklog.WorklogScreen

// ====== Public composables
@Composable
fun App() {
    val navController = rememberNavController()

    MusekitNavHost(
        navController = navController,
        modifier = Modifier,
    ) {
        composable<Main> { MusekitNestedNavigation() }
        composable<Worklog> { WorklogScreen() }
    }
}

// ====== Private composables
@Composable
private fun MusekitNavHost(
    navController: NavHostController,
    modifier: Modifier,
    builder: NavGraphBuilder.() -> Unit,
) {
    val musekitNavigation =
        MusekitNavigation(
            navigateToWorklog = { navController.navigate(Worklog) },
            navigateBack = { navController.popBackStack() },
        )

    CompositionLocalProvider(LocalMusekitNavigation provides musekitNavigation) {
        NavHost(
            navController = navController,
            startDestination = Main,
            modifier = modifier,
            builder = builder,
        )
    }
}

@Composable
private fun MusekitNestedNavigation() {
    val navController = rememberNavController()

    val navBarRoutes =
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

                navBarRoutes.forEach { route ->
                    NavigationBarItem(
                        icon = { Icon(route.icon, contentDescription = route.name) },
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
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NoteFork,
            modifier = Modifier.padding(paddingValues),
        ) {
            fadeComposable<Metronome> { MetronomeScreen() }
            fadeComposable<NoteFork> { NoteForkScreen() }
            fadeComposable<Settings> { SettingsScreen() }
        }
    }
}
