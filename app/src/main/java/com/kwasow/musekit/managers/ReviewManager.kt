package com.kwasow.musekit.managers

import kotlinx.coroutines.flow.Flow

interface ReviewManager {
    val shouldShowReviewRequest: Flow<Boolean>

    suspend fun init()

    suspend fun dismiss()

    suspend fun dismissForever()
}
