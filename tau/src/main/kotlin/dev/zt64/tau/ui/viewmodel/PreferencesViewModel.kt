package dev.zt64.tau.ui.viewmodel

import androidx.lifecycle.ViewModel
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.manager.ShortcutsManager

class PreferencesViewModel(
    private val preferences: PreferencesManager,
    private val shortcuts: ShortcutsManager
) : ViewModel() {
    var theme by preferences::theme
}