package com.kwasow.musekit.managers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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

    override val shouldShowReviewRequest: Flow<Boolean> = flowOf(false)

    override suspend fun init() {}

    override suspend fun dismiss() {}

    override suspend fun dismissForever() {}
}
