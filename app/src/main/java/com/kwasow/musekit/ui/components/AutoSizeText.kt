package com.kwasow.musekit.ui.components

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.TextViewCompat

// ====== Public composables
@SuppressLint("RestrictedApi")
@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier
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
                typeface = Typeface.DEFAULT_BOLD
                setAutoSizeTextTypeWithDefaults(TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            }
        },
        update = { view ->
            view.text = text
        }
    )
}
