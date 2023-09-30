package zt.tau.domain.manager

import com.russhwolf.settings.Settings
import zt.tau.domain.manager.base.BasePreferenceManager
import zt.tau.model.Theme
import zt.tau.model.ViewMode

class PreferencesManager : BasePreferenceManager(Settings()) {
    var theme: Theme by preference(Theme.DARK)
    var color: Int by preference(0x1ed760) // Spotify green
    var viewMode: ViewMode by preference(ViewMode.GRID)
}
