package dev.zt64.tau.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import ca.gosyer.appdirs.AppDirs
import dev.zt64.tau.domain.manager.*
import dev.zt64.tau.domain.manager.base.Settings
import dev.zt64.tau.domain.manager.base.SettingsSerializer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.io.File

val managerModule = module {
    singleOf(::NavigationManager)
    singleOf(::NotificationManager)

    single<DataStore<Settings>> {
        val appDirs = AppDirs {
            appName = "tau"
        }
        val configDir = File(appDirs.getUserConfigDir())
        configDir.mkdirs()

        DataStoreFactory.create(
            serializer = SettingsSerializer,
            produceFile = { File(configDir, "settings.json") }
        )
    }

    singleOf(::ShortcutsManager)
    singleOf(::PreferencesManager)
}