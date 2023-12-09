package dev.zt64.tau.domain.manager

import com.russhwolf.settings.Settings
import dev.zt64.tau.domain.manager.base.BasePreferenceManager
import dev.zt64.tau.model.Theme
import dev.zt64.tau.model.ViewMode

class PreferencesManager : BasePreferenceManager(Settings()) {
    var theme: Theme by preference(Theme.DARK)
    var color: Int by preference(0x1ed760) // Spotify green
    var viewMode: ViewMode by preference(ViewMode.GRID)
}
