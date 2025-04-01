package dev.zt64.tau.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.tau.domain.manager.NavigationManager
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.model.Bookmark
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import oshi.SystemInfo
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

class SidePanelViewModel(private val preferencesManager: PreferencesManager, val nav: NavigationManager) : ViewModel() {
    val roots = SystemInfo().operatingSystem.fileSystem.getFileStores(true)
        .filter { it.description == "Local Disk" } // at least gets rid of some of the clutter

    val pinned = MutableStateFlow(emptyList<Bookmark>())
    val bookmarks = pinned.asStateFlow()

    init {
        viewModelScope.launch {
            if (hostOs == OS.Windows) {
                // nothing yet
            } else {
                val userHome = System.getProperty("user.home")
                val xdgConfig = File(System.getenv("XDG_CONFIG_HOME") ?: "$userHome/.config", "user-dirs.dirs")

                // read the file and get the lines
                val lines = xdgConfig.readLines().filter { it.startsWith("XDG_") }.associate {
                    val (envVar, value) = it.split("=")

                    envVar to value.replace("\$HOME", userHome).removeSurrounding("\"")
                }

                pinned.emit(
                    lines.map { (envVar, value) ->
                        Bookmark(
                            path = Path(value),
                            name = envVar.removePrefix("XDG_").removeSuffix("_DIR").lowercase()
                                .replaceFirstChar { it.uppercase() }
                        )
                    }
                )
            }
        }
    }

    fun navigateToPath(path: Path) {
        viewModelScope.launch {
            nav.navigate(path)
        }
    }
}