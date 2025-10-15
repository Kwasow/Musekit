package com.kwasow.musekit.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

// ====== Public composables
@Composable
fun AdaptiveNavigationScaffold(
    navigationItems: @Composable AdaptiveScaffoldScope.() -> Unit,
    content: @Composable ((PaddingValues) -> Unit)
) {

}

// ====== Private composable
@Composable
private fun WideScreenScaffold(
    navigationItems: @Composable AdaptiveScaffoldScope.() -> Unit,
    content: @Composable ((PaddingValues) -> Unit)
) {

}

@Composable
private fun NarrowScreenScaffold(
    navigationItems: @Composable AdaptiveScaffoldScope.() -> Unit,
    content: @Composable ((PaddingValues) -> Unit)
) {

}
