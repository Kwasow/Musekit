package com.kwasow.musekit.extensions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

inline fun <reified T : Any> NavGraphBuilder.fadeComposable(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    val time = 200

    composable<T>(
        content = content,
        enterTransition = { fadeIn(animationSpec = tween(time, delayMillis = time)) },
        exitTransition = { fadeOut(animationSpec = tween(time)) },
        deepLinks = deepLinks,
    )
}
