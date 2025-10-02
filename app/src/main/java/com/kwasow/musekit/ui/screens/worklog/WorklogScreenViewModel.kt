package com.kwasow.musekit.ui.screens.worklog

import androidx.lifecycle.ViewModel
import com.kwasow.musekit.managers.WorklogManager
import com.kwasow.musekit.room.PracticeSession

class WorklogScreenViewModel(
    private val worklogManager: WorklogManager
) : ViewModel() {
    // ====== Public methods
    fun getPracticeSessions(): List<PracticeSession> =
        worklogManager.getPracticeSessions()
}
