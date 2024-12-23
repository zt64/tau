package dev.zt64.tau.ui.viewmodel

import androidx.lifecycle.ViewModel
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.manager.ShortcutsManager
import dev.zt64.tau.model.Shortcut

class PreferencesViewModel(val preferences: PreferencesManager, val shortcuts: ShortcutsManager) : ViewModel() {
    var theme by preferences::theme

    // Ensure that no two shortcuts are the same
    fun validateShortcut(shortcut: Shortcut): Boolean {
        return false
    }

    fun setShortcut(shortcut: Shortcut) {
    }
}