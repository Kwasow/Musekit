package com.kwasow.musekit.koin

import com.kwasow.musekit.managers.PermissionManager
import com.kwasow.musekit.managers.PermissionManagerImpl
import com.kwasow.musekit.managers.PitchPlayerManager
import com.kwasow.musekit.managers.PitchPlayerManagerImpl
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.PreferencesManagerImpl
import com.kwasow.musekit.managers.PresetsManager
import com.kwasow.musekit.managers.PresetsManagerImpl
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
    }
