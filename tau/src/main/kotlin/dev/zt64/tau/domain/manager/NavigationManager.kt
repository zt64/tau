package dev.zt64.tau.domain.manager

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.Snapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.system.exitProcess

/**
 * Manages the navigation of the application.
 */
class NavigationManager {
    private var _currentLocation = MutableStateFlow(Path("/"))
    var currentLocation = _currentLocation.asStateFlow()

    val tabs = mutableStateListOf(_currentLocation.value)

    private var _currentTabIndex = MutableStateFlow(0)
    val currentTabIndex = _currentTabIndex.asStateFlow()

    val selected = mutableStateListOf<Path>()

    // The forward and backward stacks are used to keep track of the navigation history.
    // TODO: Implement a more efficient way to handle the navigation history.
    //       Give each tab its own history
    private val forwardStack = mutableStateListOf<Path>()
    private val backwardStack = mutableStateListOf<Path>()

    val canGoUp by derivedStateOf { _currentLocation.value.parent != null }
    val canGoForward by derivedStateOf {
        forwardStack.isNotEmpty() && _currentLocation.value != forwardStack.first()
    }
    val canGoBack by derivedStateOf {
        backwardStack.isNotEmpty() && _currentLocation.value != backwardStack.first()
    }

    suspend fun navigate(path: Path) {
        backwardStack.add(_currentLocation.value)
        updateCurrentLocation(path)
    }

    suspend fun navigateUp() {
        // IDK, how to make this work with history
        // navigating up, should result in the back button becoming available, but it doesn't

        backwardStack.add(_currentLocation.value)
        updateCurrentLocation(_currentLocation.value.parent!!)
    }

    suspend fun navigateForward() {
        if (forwardStack.isEmpty() || _currentLocation.value == forwardStack.first()) {
            throw IllegalStateException("Cannot go forward")
        } else {
            backwardStack.add(_currentLocation.value)
            updateCurrentLocation(forwardStack.removeLast())
        }
    }

    suspend fun navigateBack() {
        if (backwardStack.isEmpty() || _currentLocation.value == backwardStack.first()) {
            throw IllegalStateException("Cannot go back")
        } else {
            forwardStack.add(_currentLocation.value)
            updateCurrentLocation(backwardStack.removeLast())
        }
    }

    suspend fun newTab(path: Path = currentLocation.value) {
        Snapshot.withMutableSnapshot {
            tabs.add(_currentTabIndex.value + 1, path)
            switchTab(_currentTabIndex.value + 1)
        }
    }

    suspend fun closeTab(index: Int = currentTabIndex.value) {
        if (tabs.size > 1) {
            Snapshot.withMutableSnapshot {
                tabs.removeAt(index)

                when {
                    index == _currentTabIndex.value -> switchTab((index - 1).coerceAtLeast(0))
                    index < _currentTabIndex.value -> switchTab(_currentTabIndex.value - 1)
                }
            }
        } else {
            // Close the app
            exitProcess(0)
        }
    }

    suspend fun switchTab(index: Int) {
        _currentTabIndex.update { index }
        updateCurrentLocation(tabs[index])
    }

    private suspend fun updateCurrentLocation(path: Path) {
        _currentLocation.emit(path)
    }
}