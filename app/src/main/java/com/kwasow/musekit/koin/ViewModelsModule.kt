package com.kwasow.musekit.koin

import com.kwasow.musekit.models.PresetDialogViewModel
import com.kwasow.musekit.ui.screens.fork.NoteForkFragmentViewModel
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
            NoteForkFragmentViewModel(get(), get(), get(), get())
        }

        viewModel {
            PresetDialogViewModel(get())
        }

        viewModel {
            SettingsScreenViewModel(get(), get(), get())
        }
    }
