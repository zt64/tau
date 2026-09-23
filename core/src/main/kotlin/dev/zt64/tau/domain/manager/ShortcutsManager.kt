package dev.zt64.tau.domain.manager

import androidx.compose.ui.input.key.Key
import androidx.datastore.core.DataStore
import dev.zt64.tau.domain.manager.base.BasePreferenceManager
import dev.zt64.tau.domain.manager.base.Settings
import dev.zt64.tau.domain.model.KeyModifier
import dev.zt64.tau.domain.model.Shortcut
import kotlinx.serialization.Serializable

@Serializable
data class Shortcuts(
    val selectAll: Shortcut = Shortcut(KeyModifier.Ctrl, Key.A),
    val copy: Shortcut = Shortcut(KeyModifier.Ctrl, Key.C),
    val cut: Shortcut = Shortcut(KeyModifier.Ctrl, Key.X),
    val paste: Shortcut = Shortcut(KeyModifier.Ctrl, Key.V),
    val delete: Shortcut = Shortcut(Key.Delete),
    val rename: Shortcut = Shortcut(Key.F2),
    val newFolder: Shortcut = Shortcut(KeyModifier.Ctrl, Key.N),
    val newTab: Shortcut = Shortcut(KeyModifier.Ctrl, Key.T),
    val toggleHidden: Shortcut = Shortcut(KeyModifier.Ctrl, Key.H),
    val menu: Shortcut = Shortcut(Key.AltLeft)
)

/**
 * Manages shortcuts
 */
class ShortcutsManager(settings: DataStore<Settings>) : BasePreferenceManager(settings) {
    val allShortcuts = preference(Settings::shortcuts, Shortcuts())

    val shortcutEntries = listOf(
        ShortcutEntry("Select All") { it.selectAll },
        ShortcutEntry("Copy") { it.copy },
        ShortcutEntry("Cut") { it.cut },
        ShortcutEntry("Paste") { it.paste },
        ShortcutEntry("Delete") { it.delete },
        ShortcutEntry("Rename") { it.rename },
        ShortcutEntry("New Folder") { it.newFolder },
        ShortcutEntry("New Tab") { it.newTab },
        ShortcutEntry("Toggle Hidden") { it.toggleHidden },
        ShortcutEntry("Menu") { it.menu }
    )
}

data class ShortcutEntry(
    val label: String,
    val getter: (Shortcuts) -> Shortcut
)