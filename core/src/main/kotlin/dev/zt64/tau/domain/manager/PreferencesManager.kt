package dev.zt64.tau.domain.manager

import androidx.datastore.core.DataStore
import dev.zt64.tau.domain.manager.base.BasePreferenceManager
import dev.zt64.tau.domain.manager.base.Settings

class PreferencesManager(settings: DataStore<Settings>) : BasePreferenceManager(settings) {
    val appearanceSettings = preference(Settings::appearance,Settings.AppearanceSettings())
    val behaviorSettings = preference(Settings::behavior,Settings.BehaviorSettings())
    val viewSettings = preference(Settings::view,Settings.ViewSettings())
}