package com.kwasow.musekit.managers

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri

class PermissionManagerImpl(
    private val context: Context,
) : PermissionManager {
    // ====== Interface methods
    override fun openPermissionSettings() {
        val intent =
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                ("package:" + context.packageName).toUri(),
            )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
    }
}
