package dev.zt64.tau.ui.state

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import io.github.irgaly.kfswatch.KfsDirectoryWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.*

@Stable
class BrowserState {
    var currentLocation by mutableStateOf(Path("/"), referentialEqualityPolicy())
        private set
    val selected = mutableStateListOf<Path>()
    private var forwardStack = mutableStateListOf<Path>()
    private var backwardStack = mutableStateListOf<Path>()

    var snackbarHostState = SnackbarHostState()
    val watcher = KfsDirectoryWatcher(CoroutineScope(Dispatchers.IO))
    var search by mutableStateOf("")
    val tabs = mutableStateListOf(currentLocation)
    var currentTab by mutableIntStateOf(0)

    var files by mutableStateOf(listOf<Path>())

    // TODO: Move this to business logic
    fun scanDir() {
        files = try {
            currentLocation
                .listDirectoryEntries()
                .asSequence()
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

    fun clickGrid() {
        selected.clear()
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
                    Desktop.getDesktop().open(path.toFile())
                } catch (e: IOException) {
                    e.printStackTrace()

                    showError("No default application for ${path.name}")
                }
            }

            path.isExecutable() -> {
                Desktop.getDesktop().open(path.toFile())
            }
        }
    }

    fun showError(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(message)
        }
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

    fun selectFiles(vararg paths: Path) {
        selected.clear()
        selected += paths
    }

    private fun updateCurrentLocation(path: Path) {
        currentLocation = path
        scanDir()
    }

    val canGoUp by derivedStateOf { currentLocation.parent != null }
    val canGoForward by derivedStateOf {
        forwardStack.isNotEmpty() &&
            currentLocation != forwardStack.first()
    }
    val canGoBack by derivedStateOf {
        backwardStack.isNotEmpty() &&
            currentLocation != backwardStack.first()
    }
}

@Composable
fun rememberBrowserState() = remember { BrowserState() }