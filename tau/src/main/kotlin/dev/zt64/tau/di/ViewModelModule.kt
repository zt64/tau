package dev.zt64.tau.di

import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::BrowserViewModel)
}