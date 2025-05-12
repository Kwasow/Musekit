package com.kwasow.musekit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ====== Public composables
@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onChange: (Boolean) -> Unit,
) {
    PlayPauseButton(
        modifier = modifier,
        isPlaying = isPlaying,
        onPlay = { onChange(true) },
        onPause = { onChange(false) },
    )
}

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
) {
    Box(modifier = modifier) {
        Card(
            modifier =
                Modifier
                    .size(50.dp)
                    .clickable {
                        if (isPlaying) {
                            onPause()
                        } else {
                            onPlay()
                        }
                    },
        ) {
            if (isPlaying) {
                Text("Playing")
            } else {
                Text("Paused")
            }
        }
    }
}
