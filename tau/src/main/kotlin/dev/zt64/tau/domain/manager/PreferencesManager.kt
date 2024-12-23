package dev.zt64.tau.domain.manager

import com.russhwolf.settings.PreferencesSettings
import dev.zt64.tau.domain.manager.base.BasePreferenceManager
import dev.zt64.tau.model.*

class PreferencesManager(settings: PreferencesSettings) : BasePreferenceManager(settings) {
    var theme: Theme by preference(Theme.DARK)
    var color: Int by preference(0x1ed760) // Spotify green
    var scale: Int by preference(78)

    var showMenuBar: Boolean by preference(true)
    var showToolbar: Boolean by preference(true)
    var showStatusBar: Boolean by preference(true)

    var maxNameLines: Int by preference(2)
    var openItemAction: OpenItemAction by preference(OpenItemAction.DOUBLE_CLICK)
    var truncateNames: Boolean by preference(true)
    var showHiddenFiles: Boolean by preference(false)

    // var columns by preference()
    var autoSizeColumns: Boolean by preference(true)

    var sortType: DetailColumnType by preference(DetailColumnType.NAME)
    var sortDirection: Direction by preference(Direction.DESCENDING)

    // Confirmations
    var confirmEmptyTrash by preference(false)
    var confirmDeleteItems by preference(false)
    var confirmExitWithTabs by preference(false)
}