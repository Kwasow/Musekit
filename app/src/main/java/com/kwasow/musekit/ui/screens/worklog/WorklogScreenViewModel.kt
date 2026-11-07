package com.kwasow.musekit.ui.screens.worklog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwasow.musekit.managers.WorklogManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WorklogScreenViewModel(
    worklogManager: WorklogManager,
) : ViewModel() {
    // ====== Fields
    val practiceSessions: StateFlow<List<MonthPracticeSessions>> =
        worklogManager.practiceSessions
            .map { sessions ->
                sessions
                    .groupBy { (date) -> Pair(date.year, date.monthValue) }
                    .map { (yearMonth, sessions) ->
                        MonthPracticeSessions(
                            year = yearMonth.first,
                            month = yearMonth.second,
                            sessions = sessions.sortedByDescending { it.date },
                        )
                    }.sortedByDescending { (year, month) -> year * 12 + month }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
