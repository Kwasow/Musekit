package com.kwasow.musekit.managers

interface ReviewManager {
    suspend fun init()

    suspend fun shouldShowReviewRequest(): Boolean

    suspend fun dismiss()

    suspend fun dismissForever()
}
