package dev.zt64.tau.di

import com.russhwolf.settings.PreferencesSettings
import dev.zt64.tau.domain.manager.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managerModule = module {
    singleOf(::NavigationManager)
    singleOf(::NotificationManager)
    fun providePreferencesSettings(): PreferencesSettings {
        return PreferencesSettings.Factory().create("tau")
    }

    singleOf(::providePreferencesSettings)
    singleOf(::ShortcutsManager)
    singleOf(::PreferencesManager)
}