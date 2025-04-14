package com.kwasow.musekit.koin

import com.kwasow.musekit.models.MetronomeFragmentViewModel
import com.kwasow.musekit.models.NoteForkFragmentViewModel
import com.kwasow.musekit.models.PresetDialogViewModel
import com.kwasow.musekit.models.SettingsFragmentViewModel
import com.kwasow.musekit.models.ThemeDialogViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule =
    module {
        viewModel {
            MetronomeFragmentViewModel(get())
        }

        viewModel {
            NoteForkFragmentViewModel(get(), get(), get(), get())
        }

        viewModel {
            PresetDialogViewModel(get())
        }

        viewModel {
            SettingsFragmentViewModel(get())
        }

        viewModel {
            ThemeDialogViewModel(get(), get())
        }
    }
