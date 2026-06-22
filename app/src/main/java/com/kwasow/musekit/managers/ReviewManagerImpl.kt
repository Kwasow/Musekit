package com.kwasow.musekit.managers

import com.kwasow.musekit.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar

/**
 * The idea is to count on how many different days a user has opened the app. If they opened the app
 * on more than 5 different days, we can show them an unobtrusive nudge to leave a review. That is,
 * if they are using the Play Store version of the app - the F-droid app never shows that.
 */
class ReviewManagerImpl(
    val preferencesManager: PreferencesManager,
) : ReviewManager {
    companion object {
        const val DISMISS_FOREVER: Long = -1
    }

    override val shouldShowReviewRequest: Flow<Boolean> =
        preferencesManager.daysUsedCounter.map { uniqueDays ->
            return@map BuildConfig.DISTRIBUTION_CHANNEL != "foss" && uniqueDays > 5
        }

    override suspend fun init() {
        val lastUsedTimestamp = preferencesManager.lastUsedTimestamp.first()
        val daysUsedCounter = preferencesManager.daysUsedCounter.first()

        val lastUsed = Calendar.getInstance().apply { timeInMillis = lastUsedTimestamp }
        val today = Calendar.getInstance()

        val differentDay =
            lastUsed.get(Calendar.YEAR) != today.get(Calendar.YEAR) ||
                lastUsed.get(Calendar.MONTH) != today.get(Calendar.MONTH) ||
                lastUsed.get(Calendar.DAY_OF_MONTH) != today.get(Calendar.DAY_OF_MONTH)

        if (differentDay && daysUsedCounter != DISMISS_FOREVER) {
            preferencesManager.setDaysUsedCounter(daysUsedCounter + 1)
            preferencesManager.setLastUsedTimestamp(today.timeInMillis)
        }
    }

    override suspend fun dismiss() = preferencesManager.setDaysUsedCounter(0)

    override suspend fun dismissForever() = preferencesManager.setDaysUsedCounter(DISMISS_FOREVER)
}
