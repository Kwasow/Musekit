package com.kwasow.musekit.managers

import android.content.Context
import android.util.Log
import com.kwasow.musekit.BuildConfig
import com.kwasow.musekit.store.musekitPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

class UpdateManagerImpl(
    val context: Context,
) : UpdateManager {
    // ====== Fields
    private var lastVersionCode by Delegates.notNull<Long>()
    private val currentVersionCode = BuildConfig.VERSION_CODE.toLong()

    // ====== Interface methods
    override fun init() {
        // Read last version
        lastVersionCode =
            runBlocking {
                context.musekitPreferencesDataStore.data
                    .map { preferences ->
                        preferences.lastVersionCode
                    }.first()
            }

        Log.d("UpdateManager", "Last version code: $lastVersionCode")

        // Run updates

        // Update last version
        runBlocking {
            updateLastVersionCode()
        }
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
