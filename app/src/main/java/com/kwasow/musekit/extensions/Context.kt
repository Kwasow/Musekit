package com.kwasow.musekit.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.preventSleep() {
    val window = this.findActivity()?.window
    window?.preventSleep()
}

fun Context.allowSleep() {
    val window = this.findActivity()?.window
    window?.allowSleep()
}

fun Context.findActivity(): Activity? {
    var context = this

    while (context is ContextWrapper) {
        if (context is Activity) return context

        context = context.baseContext
    }

    return null
}
