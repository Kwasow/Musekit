package com.kwasow.musekit.ui.screens.worklog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwasow.musekit.managers.WorklogManager
import com.kwasow.musekit.room.PracticeSession
import kotlinx.coroutines.launch

class WorklogScreenViewModel(
    private val worklogManager: WorklogManager
) : ViewModel() {
    // ====== Fields
    val practiceSessions: MutableLiveData<List<PracticeSession>?> = MutableLiveData()

    // ====== Public methods
    fun getPracticeSessions() {
        viewModelScope.launch {
            practiceSessions.postValue(worklogManager.getPracticeSessions())
        }
    }
}
