package com.kwasow.musekit.koin

import com.kwasow.musekit.managers.PermissionManager
import com.kwasow.musekit.managers.PermissionManagerImpl
import com.kwasow.musekit.managers.PitchPlayerManager
import com.kwasow.musekit.managers.PitchPlayerManagerImpl
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.PreferencesManagerImpl
import com.kwasow.musekit.managers.PresetsManager
import com.kwasow.musekit.managers.PresetsManagerImpl
import com.kwasow.musekit.managers.UpdateManager
import com.kwasow.musekit.managers.UpdateManagerImpl
import com.kwasow.musekit.managers.WorklogManager
import com.kwasow.musekit.managers.WorklogManagerImpl
import org.koin.dsl.module

val managersModule =
    module {
        single<PermissionManager> {
            PermissionManagerImpl(get())
        }

        single<PitchPlayerManager> {
            PitchPlayerManagerImpl()
        }

        single<PreferencesManager> {
            PreferencesManagerImpl(get())
        }

        single<PresetsManager> {
            PresetsManagerImpl(get())
        }

        single<UpdateManager> {
            UpdateManagerImpl(get())
        }

        single<WorklogManager> {
            WorklogManagerImpl()
        }
    }
