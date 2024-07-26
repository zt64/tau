package dev.zt64.tau.di

import com.russhwolf.settings.PreferencesSettings
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.manager.ShortcutsManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managerModule = module {
    fun providePreferencesSettings(): PreferencesSettings {
        return PreferencesSettings.Factory().create("tau")
    }

    singleOf(::providePreferencesSettings)
    singleOf(::PreferencesManager)
    singleOf(::ShortcutsManager)
}