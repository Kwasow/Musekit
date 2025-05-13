package com.kwasow.musekit.koin

import com.kwasow.musekit.ui.screens.fork.NoteForkScreenViewModel
import com.kwasow.musekit.ui.screens.metronome.MetronomeScreenViewModel
import com.kwasow.musekit.ui.screens.settings.SettingsScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule =
    module {
        viewModel {
            MetronomeScreenViewModel(get())
        }

        viewModel {
            NoteForkScreenViewModel(get(), get(), get(), get(), get())
        }

        viewModel {
            SettingsScreenViewModel(get(), get())
        }
    }
