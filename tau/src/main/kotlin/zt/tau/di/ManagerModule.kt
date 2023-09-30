package zt.tau.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import zt.tau.domain.manager.PreferencesManager

val managerModule = module {
    singleOf(::PreferencesManager)
}