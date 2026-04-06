package com.kwasow.musekit.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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
import com.kwasow.musekit.utils.ScreenUtils

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
                rememberVectorPainter(Icons.Outlined.MusicNote),
            ),
            TopLevelRoute(
                stringResource(id = R.string.metronome),
                Metronome,
                painterResource(R.drawable.ic_metronome),
            ),
            TopLevelRoute(
                stringResource(id = R.string.settings),
                Settings,
                rememberVectorPainter(Icons.Outlined.Settings),
            ),
        )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            musekitMainNavigation(
                navController = navController,
                topLevelRoutes = topLevelRoutes,
                currentDestination = currentDestination,
            )
        },
        layoutType = musekitMainNavigationSuiteType(currentDestination),
    ) {
        MainContent(navController = navController)
    }
}

@Composable
private fun MainContent(navController: NavHostController) {
    val musekitNavigation =
        MusekitNavigation(
            navigateToWorklog = { navController.navigate(Worklog) },
            navigateBack = { navController.popBackStack() },
        )

    CompositionLocalProvider(LocalMusekitNavigation provides musekitNavigation) {
        NavHost(
            navController = navController,
            startDestination = NoteFork,
        ) {
            fadeComposable<NoteFork> { NoteForkScreen() }
            fadeComposable<Metronome> { MetronomeScreen() }
            fadeComposable<Worklog> { WorklogScreen() }
            fadeComposable<Settings> { SettingsScreen() }
        }
    }
}

@Composable
private fun musekitMainNavigationSuiteType(
    currentDestination: NavDestination?,
): NavigationSuiteType {
    val adaptiveInfo = currentWindowAdaptiveInfo()

    return if (
        currentDestination?.hierarchy?.any {
            it.hasRoute(Worklog::class)
        } == true
    ) {
        NavigationSuiteType.None
    } else if (ScreenUtils.isWide()) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
    }
}

private fun NavigationSuiteScope.musekitMainNavigation(
    navController: NavController,
    topLevelRoutes: List<TopLevelRoute<*>>,
    currentDestination: NavDestination?,
) {
    topLevelRoutes.forEach { (name, route, icon) ->
        item(
            icon = {
                Icon(
                    painter = icon,
                    contentDescription = name,
                )
            },
            label = { Text(name) },
            selected =
                currentDestination?.hierarchy?.any {
                    it.hasRoute(route::class)
                } == true,
            onClick = {
                navController.navigate(route) {
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
