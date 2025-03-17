package com.kwasow.musekit.koin

import com.kwasow.musekit.managers.PermissionManager
import com.kwasow.musekit.managers.PermissionManagerImpl
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.PreferencesManagerImpl
import com.kwasow.musekit.managers.PresetsManager
import com.kwasow.musekit.managers.PresetsManagerImpl
import com.kwasow.musekit.managers.ThemeManager
import com.kwasow.musekit.managers.ThemeManagerImpl
import org.koin.dsl.module

val managersModule =
    module {
        single<PermissionManager> {
            PermissionManagerImpl(get())
        }

        single<PreferencesManager> {
            PreferencesManagerImpl(get())
        }

        single<PresetsManager> {
            PresetsManagerImpl(get())
        }

        single<ThemeManager> {
            ThemeManagerImpl(get(), get())
        }
    }
