package com.kwasow.musekit.ui.screens.metronome

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.services.MetronomeService
import com.kwasow.musekit.utils.MusekitBeatDetector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MetronomeScreenViewModel(
    private val applicationContext: Context,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ====== Fields
    private val beatDetector by lazy { MusekitBeatDetector() }

    private val service = MutableStateFlow<MetronomeService?>(null)

    private val serviceConnection =
        object : ServiceConnection {
            override fun onServiceConnected(
                className: ComponentName,
                binder: IBinder,
            ) {
                service.value = (binder as MetronomeService.LocalBinder).service
            }

            override fun onServiceDisconnected(className: ComponentName) {
                service.value = null
            }
        }

    val metronomeSound =
        preferencesManager.metronomeSound.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            MetronomeSounds.Default,
        )
    val metronomeBpm =
        preferencesManager.metronomeBpm.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null,
        )
    val metronomeNumberOfBeats =
        preferencesManager.metronomeNumberOfBeats.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null,
        )

    val isPlaying =
        service
            .flatMapLatest { it?.isPlaying ?: flowOf(false) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val currentBeat =
        service
            .flatMapLatest { it?.currentBeat ?: flowOf(0) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // ====== Constructors
    init {
        applicationContext.bindService(
            Intent(applicationContext, MetronomeService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE,
        )
    }

    // ====== Interface methods
    override fun onCleared() {
        applicationContext.unbindService(serviceConnection)
        service.value = null
    }

    // ====== Public methods
    fun beatEvent(): Int? = beatDetector.beatEvent()

    fun setBpm(value: Int) =
        viewModelScope.launch {
            preferencesManager.setMetronomeBpm(value)
        }

    fun setSound(sound: MetronomeSounds) =
        viewModelScope.launch {
            preferencesManager.setMetronomeSound(sound)
        }

    fun setNumberOfBeats(value: Int) =
        viewModelScope.launch {
            preferencesManager.setMetronomeNumberOfBeats(value)
        }

    fun startStopMetronome() {
        service.value?.startStopMetronome()
    }
}
