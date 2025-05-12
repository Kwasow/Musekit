package com.kwasow.musekit.ui.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString

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
    BasicText(
        text = text,
        modifier = modifier,
        autoSize = TextAutoSize.StepBased(),
        maxLines = 1,
    )
}
