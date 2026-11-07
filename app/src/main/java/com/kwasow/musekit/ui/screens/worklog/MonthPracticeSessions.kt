package com.kwasow.musekit.ui.screens.worklog

import com.kwasow.musekit.room.PracticeSession

data class MonthPracticeSessions(
    val year: Int,
    val month: Int,
    val sessions: List<PracticeSession>,
)
