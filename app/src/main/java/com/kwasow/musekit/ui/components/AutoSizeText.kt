package com.kwasow.musekit.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight

// ====== Public composables
@Composable
fun AutoSizeText(
    text: String,
    boldFont: Boolean,
    modifier: Modifier = Modifier,
) {
    AutoSizeText(
        text = AnnotatedString(text),
        boldFont = boldFont,
        modifier = modifier,
    )
}

@Composable
fun AutoSizeText(
    text: AnnotatedString,
    boldFont: Boolean,
    modifier: Modifier = Modifier,
) {
    var textStyle =
        MaterialTheme.typography.displayMedium.copy(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
        )
    if (boldFont) {
        textStyle = textStyle.copy(fontWeight = FontWeight.Bold)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = textStyle,
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(),
        )
    }
}
