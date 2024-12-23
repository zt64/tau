package dev.zt64.tau.di

import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import dev.zt64.tau.ui.viewmodel.PreferencesViewModel
import dev.zt64.tau.ui.viewmodel.SidePanelViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::BrowserViewModel)
    viewModelOf(::SidePanelViewModel)
    viewModelOf(::PreferencesViewModel)
}