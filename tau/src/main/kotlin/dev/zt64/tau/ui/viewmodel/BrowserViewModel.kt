package dev.zt64.tau.ui.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.util.SortDirection
import dev.zt64.tau.util.SortType
import dev.zt64.tau.util.creationTime
import io.github.irgaly.kfswatch.KfsDirectoryWatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.*

class BrowserViewModel(private val pref: PreferencesManager) : ViewModel() {
    private val desktop by lazy(Desktop::getDesktop)

    var currentLocation by mutableStateOf(Path("/"), referentialEqualityPolicy())
        private set

    val tabs = mutableStateListOf(currentLocation)

    private var _currentTabIndex = MutableStateFlow(0)
    val currentTabIndex = _currentTabIndex.asStateFlow()

    val snackbarHostState = SnackbarHostState()

    /** The contents of the current directory */
    var contents = mutableStateListOf<Path>()
        private set

    val watcher = KfsDirectoryWatcher(viewModelScope)
    var searching by mutableStateOf(false)
    var search by mutableStateOf("")
        private set

    // TODO: Use indices instead of paths to use less memory
    val selected = mutableStateListOf<Path>()
    private val forwardStack = mutableStateListOf<Path>()
    private val backwardStack = mutableStateListOf<Path>()

    val canGoUp by derivedStateOf { currentLocation.parent != null }
    val canGoForward by derivedStateOf {
        forwardStack.isNotEmpty() && currentLocation != forwardStack.first()
    }
    val canGoBack by derivedStateOf {
        backwardStack.isNotEmpty() && currentLocation != backwardStack.first()
    }

    init {
        refresh()
    }

    fun navigate(path: Path) {
        backwardStack.add(currentLocation)
        updateCurrentLocation(path)
    }

    fun navigateUp() {
        // IDK, how to make this work with history
        // navigating up, should result in the back button becoming available, but it doesn't

        backwardStack.add(currentLocation)
        updateCurrentLocation(currentLocation.parent!!)
    }

    fun navigateForward() {
        if (forwardStack.isEmpty() || currentLocation == forwardStack.first()) {
            throw IllegalStateException("Cannot go forward")
        } else {
            backwardStack.add(currentLocation)
            updateCurrentLocation(forwardStack.removeLast())
        }
    }

    fun navigateBack() {
        if (backwardStack.isEmpty() || currentLocation == backwardStack.first()) {
            throw IllegalStateException("Cannot go back")
        } else {
            forwardStack.add(currentLocation)
            updateCurrentLocation(backwardStack.removeLast())
        }
    }

    fun newTab(path: Path = currentLocation) {
        Snapshot.withMutableSnapshot {
            tabs.add(_currentTabIndex.value, path)
            switchTab(_currentTabIndex.value + 1)
        }
    }

    fun closeTab(index: Int = currentTabIndex.value) {
        Snapshot.withMutableSnapshot {
            tabs.removeAt(index)

            when {
                index == _currentTabIndex.value -> {
                    switchTab((index - 1).coerceAtLeast(0))
                }
                index < _currentTabIndex.value -> {
                    switchTab(_currentTabIndex.value - 1)
                }
            }
        }
    }

    fun switchTab(index: Int) {
        _currentTabIndex.update { index }
        updateCurrentLocation(tabs[index])
    }

    fun search(query: String) {
        search = query
        refresh()
    }

    fun clearSearch() {
        search("")
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val newItems = currentLocation
                    .listDirectoryEntries()
                    .asSequence()
                    .filter { (!it.isHidden() || pref.showHiddenFiles) && (search in it.name) }
                    .sortedWith(
                        compareBy<Path> {
                            when (pref.sortType) {
                                SortType.SIZE -> it.fileSize()
                                SortType.CREATION_DATE -> it.creationTime()
                                SortType.NAME -> it.nameWithoutExtension
                                SortType.MODIFICATION_DATE -> it.getLastModifiedTime().toInstant()
                            }
                        }.let { if (pref.sortDirection == SortDirection.DESCENDING) it.reversed() else it }
                    )
                    .toList()
                contents.clear()
                contents += newItems
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun copy() {
    }

    fun paste() {
    }

    fun selectItems(
        vararg paths: Path,
        clear: Boolean = true
    ) {
        if (clear) clearSelection()
        selected += paths
    }

    fun selectAll() {
        selectItems(*contents.toTypedArray())
    }

    fun clearSelection() {
        selected.clear()
    }

    private fun updateCurrentLocation(path: Path) {
        currentLocation = path
        tabs[_currentTabIndex.value] = path
        refresh()
    }

    fun showError(message: String) {
        viewModelScope.launch {
            // snackbarHostState.showSnackbar(message)
        }
    }

    fun open(path: Path) {
        when {
            path.isDirectory() -> {
                if (path.isReadable()) {
                    navigate(path)
                } else {
                    showError("No permission to read ${path.name}")
                }
            }
            path.isRegularFile() -> {
                try {
                    desktop.open(path.toFile())
                } catch (e: IOException) {
                    e.printStackTrace()

                    showError("No default application for ${path.name}")
                }
            }
            path.isExecutable() -> {
                desktop.open(path.toFile())
            }
        }
    }
}