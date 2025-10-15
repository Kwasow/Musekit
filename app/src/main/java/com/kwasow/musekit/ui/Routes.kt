package com.kwasow.musekit.ui

import androidx.compose.ui.graphics.painter.Painter
import kotlinx.serialization.Serializable

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: Painter)

// ====== Main routes
@Serializable
object Main

@Serializable
object Worklog

// ====== Nested routes
@Serializable
object Metronome

@Serializable
object NoteFork

@Serializable
object Settings
