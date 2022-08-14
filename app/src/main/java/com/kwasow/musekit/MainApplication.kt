package com.kwasow.musekit

import android.app.Application
import com.google.android.material.color.DynamicColors

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    // Use material3 dynamic colors
    DynamicColors.applyToActivitiesIfAvailable(this)
  }
}