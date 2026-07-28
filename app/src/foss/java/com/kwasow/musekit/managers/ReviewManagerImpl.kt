package com.kwasow.musekit.managers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Never show a review request in the FOSS build.
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
