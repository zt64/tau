package dev.zt64.tau.domain.manager

import com.russhwolf.settings.PreferencesSettings
import dev.zt64.tau.domain.manager.base.BasePreferenceManager
import dev.zt64.tau.model.Theme
import dev.zt64.tau.model.ViewMode

class PreferencesManager : BasePreferenceManager(PreferencesSettings.Factory().create("tau")) {
    var theme: Theme by preference(Theme.DARK)
    var color: Int by preference(0x1ed760) // Spotify green
    var viewMode: ViewMode by preference(ViewMode.GRID)
    var scale: Int by preference(78)

    var maxNameLines: Int by preference(2)
    var truncateNames: Boolean by preference(true)
    var showHiddenFiles by preference(false)
}