package com.kwasow.musekit.managers

import android.content.Context
import android.util.Log
import com.kwasow.musekit.BuildConfig
import com.kwasow.musekit.store.musekitPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.properties.Delegates

class UpdateManagerImpl(
    val context: Context,
) : UpdateManager {
    // ====== Fields
    private var lastVersionCode by Delegates.notNull<Long>()
    private val currentVersionCode = BuildConfig.VERSION_CODE.toLong()

    // ====== Interface methods
    override suspend fun init() {
        // Read last version
        lastVersionCode =
            context.musekitPreferencesDataStore.data
                .map { preferences ->
                    preferences.lastVersionCode
                }.first()

        // Run updates
        Log.i(
            "Update manager",
            "Last version code: $lastVersionCode, current version code: $currentVersionCode",
        )

        // Update last version
        updateLastVersionCode()
    }

    // ====== Private methods
    private fun onUpdate(
        to: Long,
        run: () -> Unit,
    ) {
        if (lastVersionCode < to && to <= currentVersionCode) {
            run()
        }
    }

    private suspend fun updateLastVersionCode() {
        context.musekitPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setLastVersionCode(BuildConfig.VERSION_CODE.toLong())
                .build()
        }
    }
}
