package dev.zt64.tau.di

import dev.zt64.tau.ui.viewmodel.BrowserScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val screenModelModule = module {
    factoryOf(::BrowserScreenModel)
}