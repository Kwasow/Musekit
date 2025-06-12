package com.kwasow.musekit.extensions

import android.view.Window
import android.view.WindowManager

fun Window.preventSleep() {
    addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun Window.allowSleep() {
    clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}
