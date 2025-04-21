package com.kwasow.musekit.ui.components

import android.graphics.Typeface
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.TextViewCompat

// ====== Public composables
@Composable
fun AutoSizeText(
    text: CharSequence,
    boldFont: Boolean,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AppCompatTextView(context).apply {
                setText(text)
                width = 0
                height = 0
                maxLines = 1
                gravity = Gravity.CENTER
                typeface = when (boldFont) {
                    true -> Typeface.DEFAULT_BOLD
                    false -> Typeface.DEFAULT
                }

                TextViewCompat.setAutoSizeTextTypeWithDefaults(
                    this,
                    TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM,
                )
            }
        },
        update = { view ->
            view.text = text
        },
    )
}
