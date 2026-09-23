package dev.zt64.tau.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.tau.domain.manager.NavigationManager
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.model.Bookmark
import dev.zt64.tau.ui.widget.Section
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

    private val _pinned = MutableStateFlow(emptyList<Bookmark>())
    val pinned = _pinned.asStateFlow()

    val sections = listOf(Section.PLACES, Section.RECENT, Section.DEVICES)

    init {
        viewModelScope.launch {
            if (hostOs == OS.Windows) {
                // nothing yet
            } else {
                val userHome = System.getProperty("user.home")
                val xdgConfig = File(System.getenv("XDG_CONFIG_HOME") ?: "$userHome/.config", "user-dirs.dirs")

                // read the file and get the lines
                val lines = xdgConfig.readLines().filter { it.startsWith("XDG_") }.map {
                    val (envVar, value) = it.split("=")

                    Bookmark(
                        path = Path(value.replace($$"$HOME", userHome).removeSurrounding("\"")),
                        name = envVar.removePrefix("XDG_").removeSuffix("_DIR").lowercase()
                            .replaceFirstChar { it.uppercase() }
                    )
                }

                _pinned.emit(lines)
            }
        }
    }

    fun navigateToPath(path: Path) {
        viewModelScope.launch {
            nav.navigate(path)
        }
    }
}