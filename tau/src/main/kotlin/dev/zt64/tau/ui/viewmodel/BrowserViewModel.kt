package dev.zt64.tau.ui.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.tau.domain.manager.PreferencesManager
import io.github.irgaly.kfswatch.KfsDirectoryWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    var currentTabIndex by mutableIntStateOf(0)

    var snackbarHostState = SnackbarHostState()
    var files by mutableStateOf(listOf<Path>())

    val watcher = KfsDirectoryWatcher(CoroutineScope(Dispatchers.IO))
    var search by mutableStateOf("")
        private set

    val selected = mutableStateListOf<Path>()
    private var forwardStack = mutableStateListOf<Path>()
    private var backwardStack = mutableStateListOf<Path>()

    val canGoUp by derivedStateOf { currentLocation.parent != null }

    val canGoForward by derivedStateOf {
        forwardStack.isNotEmpty() &&
            currentLocation != forwardStack.first()
    }
    val canGoBack by derivedStateOf {
        backwardStack.isNotEmpty() &&
            currentLocation != backwardStack.first()
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
        // navigating up, should result in the back button becoming available but it doesn't

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
        tabs.add(path)
        currentTabIndex = tabs.size - 1
    }

    fun closeTab(index: Int = currentTabIndex) {
        // TODO: Make this close the window when there is only one tab
        tabs.removeAt(index)
        currentTabIndex = tabs.size - 1
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
            files = try {
                currentLocation
                    .listDirectoryEntries()
                    .asSequence()
                    .filter { !it.isHidden() || pref.showHiddenFiles }
                    .filter { search in it.name }
                    .sortedBy { it.nameWithoutExtension }
                    .sortedBy { !it.isDirectory() }
                    .sortedBy { it.startsWith(".") }
                    .toList()
            } catch (e: IOException) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    fun selectItems(vararg paths: Path) {
        clearSelection()
        selected += paths
    }

    fun clearSelection() {
        selected.clear()
    }

    private fun updateCurrentLocation(path: Path) {
        currentLocation = path
        refresh()
    }

    fun showError(message: String) {
        viewModelScope.launch {
            // snackbarHostState.showSnackbar(message)
        }
    }

    fun doubleClick(path: Path) {
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