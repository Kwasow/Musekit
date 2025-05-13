package com.kwasow.musekit.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import com.kwasow.musekit.utils.MorphPolygonShape

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
        MorphingContainer(
            state = isPlaying,
            onPlay = onPlay,
            onPause = onPause,
        ) {
            if (isPlaying) {
                Text("Playing")
            } else {
                Text("Paused")
            }
        }
    }
}

// ====== Private composables
@Composable
private fun MorphingContainer(
    state: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    content: @Composable () -> Unit,
) {
    val shapeA = remember {
        RoundedPolygon(
            numVertices = 6,
            rounding = CornerRounding(0.2f)
        )
    }
    val shapeB = remember {
        RoundedPolygon.star(
            numVerticesPerRadius = 12,
            rounding = CornerRounding(0.1f),
            radius = 1f,
            innerRadius = 0.9f,
        )
    }
    val morph = remember { Morph(shapeA, shapeB) }

    val animatedProgress = animateFloatAsState(
        targetValue = if (state) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp)
            .clip(MorphPolygonShape(morph, animatedProgress.value))
            .background(MaterialTheme.colorScheme.secondary)
            .size(200.dp)
            .clickable {
                if (state) {
                    onPause()
                } else {
                    onPlay()
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
