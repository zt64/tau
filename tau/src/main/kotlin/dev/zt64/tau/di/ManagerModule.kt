package dev.zt64.tau.di

import dev.zt64.tau.domain.manager.PreferencesManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managerModule = module {
    singleOf(::PreferencesManager)
}