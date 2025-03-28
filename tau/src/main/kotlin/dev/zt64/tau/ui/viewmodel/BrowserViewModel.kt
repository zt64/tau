package dev.zt64.tau.ui.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.tau.domain.manager.NavigationManager
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.model.DetailColumnType
import dev.zt64.tau.model.Direction
import dev.zt64.tau.util.creationTime
import dev.zt64.tau.util.size
import io.github.irgaly.kfswatch.KfsDirectoryWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.IOException
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributeView
import kotlin.io.path.*

class BrowserViewModel(private val pref: PreferencesManager, val nav: NavigationManager) : ViewModel() {
    private val desktop by lazy(Desktop::getDesktop)

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
    var sortType by mutableStateOf(pref.sortType)
    var sortDirection by mutableStateOf(pref.sortDirection)
    var viewMode by mutableStateOf(pref.viewMode)

    init {
        viewModelScope.launch {
            nav.currentLocation.collectLatest {
                refresh()
            }
        }
    }

    fun navigate(path: Path) {
        viewModelScope.launch {
            nav.navigate(path)
        }
    }

    fun navigateUp() {
        viewModelScope.launch {
            nav.navigateUp()
        }
    }

    fun navigateForward() {
        viewModelScope.launch {
            nav.navigateForward()
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            nav.navigateBack()
        }
    }

    fun newTab(path: Path = nav.currentLocation.value) {
        viewModelScope.launch {
            nav.newTab(path)
        }
    }

    fun closeTab(index: Int = nav.currentTabIndex.value) {
        viewModelScope.launch {
            nav.closeTab(index)
        }
    }

    fun switchTab(index: Int) {
        viewModelScope.launch {
            nav.switchTab(index)
        }
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
                val newItems = nav.currentLocation.value
                    .listDirectoryEntries()
                    .asSequence()
                    .filter { (!it.isHidden() || pref.showHiddenFiles) && (search.lowercase() in it.name.lowercase()) }
                    .sortedWith(
                        compareBy<Path>(
                            { !it.isDirectory() },
                            {
                                when (sortType) {
                                    DetailColumnType.NAME -> it.nameWithoutExtension
                                    DetailColumnType.SIZE -> it.size()
                                    DetailColumnType.DATE_CREATED -> it.creationTime()
                                    DetailColumnType.DATE_MODIFIED -> it.getLastModifiedTime().toInstant()
                                    DetailColumnType.DATE_ACCESSED -> it.fileAttributesView<BasicFileAttributeView>().readAttributes().lastAccessTime().toInstant()
                                    DetailColumnType.PERMISSIONS -> TODO()
                                    DetailColumnType.OWNER -> it.getOwner()?.name
                                    DetailColumnType.GROUP -> TODO()
                                    DetailColumnType.TYPE -> it.extension
                                }
                            }
                        ).let { if (sortDirection == Direction.DESCENDING) it else it.reversed() }
                    )
                    .toList()

                contents.clear()
                contents += newItems
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun sortBy(
        type: DetailColumnType
    ) {
        if (sortType == type) {
            sortDirection = if (sortDirection == Direction.ASCENDING) Direction.DESCENDING else Direction.ASCENDING
        } else {
            sortType = type
            sortDirection = Direction.ASCENDING
        }

        refresh()
    }

    fun copy() {
    }

    fun paste() {
    }

    fun selectItems(vararg paths: Path, clear: Boolean = true) {
        if (clear) clearSelection()
        selected += paths
    }

    fun selectAll() {
        selectItems(*contents.toTypedArray())
    }

    fun clearSelection() {
        selected.clear()
    }

    fun showError(message: String) {
        viewModelScope.launch {
            // snackbarHostState.showSnackbar(message)
        }
    }

    suspend fun open(path: Path) {
        withContext(Dispatchers.IO) {
            when {
                path.isDirectory() -> {
                    if (path.isReadable()) {
                        nav.navigate(path)
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
}