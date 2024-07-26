package dev.zt64.tau.ui.viewmodel

import androidx.lifecycle.ViewModel
import dev.zt64.tau.domain.manager.PreferencesManager
import oshi.SystemInfo

class SidePanelViewModel(private val preferencesManager: PreferencesManager) : ViewModel() {
    val roots = SystemInfo().operatingSystem.fileSystem.getFileStores(false)
}