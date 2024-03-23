package dev.zt64.tau.ui.window

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.component.*
import dev.zt64.tau.ui.state.BrowserState
import dev.zt64.tau.ui.state.rememberBrowserState
import dev.zt64.tau.ui.viewmodel.BrowserScreenModel
import dev.zt64.tau.util.humanReadableSize
import dev.zt64.tau.util.moveTo
import dev.zt64.tau.util.setContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import org.koin.compose.koinInject
import java.io.File
import kotlin.io.path.absolutePathString
import kotlin.io.path.name

@OptIn(ExperimentalComposeUiApi::class, ExperimentalSplitPaneApi::class)
@Composable
fun BrowserWindow(state: BrowserState = rememberBrowserState()) {
    val preferencesManager = koinInject<PreferencesManager>()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = Modifier.onKeyEvent {
            if (it.type != KeyEventType.KeyUp) return@onKeyEvent false

            when {
                it.isCtrlPressed -> {
                    when (it.key) {
                        Key.C -> {
                            val selectedFiles = state.selected
                            val files = selectedFiles.map { it.toFile() }

                            clipboardManager.setContents(FileTransferable(files), null)
                            // Copy
                        }

                        Key.V -> {
                            // Paste
                        }

                        Key.X -> {
                            // Cut
                        }

                        Key.A -> {
                            // state.selectAll()
                        }

                        Key.R -> {
                            state.scanDir()
                        }

                        Key.H -> {
                            preferencesManager.showHiddenFiles = !preferencesManager.showHiddenFiles
                            state.scanDir()
                        }

                        Key.F -> {
                            // state.focusSearch()
                        }

                        Key.T -> {
                            state.tabs.add(state.currentLocation)
                            state.currentTab = state.tabs.size - 1
                        }

                        Key.W -> {
                            state.tabs.removeAt(state.currentTab)
                            state.currentTab = (state.currentTab - 1).coerceAtLeast(0)
                        }

                        Key.N -> {
                            // Open new window
                        }

                        else -> return@onKeyEvent false
                    }
                }

                it.key == Key.Enter -> {
                    // Open file
                }

                else -> return@onKeyEvent false
            }

            true
        },
        snackbarHost = { SnackbarHost(state.snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .onExternalDrag {
                    val dragData = it.dragData
                    if (dragData !is DragData.FilesList) {
                        println("Unsupported drag data: $dragData")
                        return@onExternalDrag
                    }

                    val currentLocation = state.currentLocation.toFile()

                    dragData.readFiles().forEach { path ->
                        File(path).moveTo(currentLocation)
                    }
                }
        ) {
            val watcher = state.watcher

            val coroutineScope = rememberCoroutineScope()

            DisposableEffect(state.currentLocation) {
                coroutineScope.launch(Dispatchers.IO) {
                    watcher.add(state.currentLocation.absolutePathString())
                    watcher.onEventFlow.collectLatest {
                        state.scanDir()
                    }
                }

                onDispose {
                    coroutineScope.launch {
                        watcher.removeAll()
                    }
                }
            }

            Toolbar(state)

            HorizontalSplitPane(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true),
                splitPaneState = rememberSplitPaneState(0.2f)
            ) {
                first(minSize = 120.dp) {
                    SidePanel(state)
                }

                second {
                    Column {
                        TabsRow(state)

                        FileGrid(
                            modifier = Modifier.weight(1f, true),
                            state = state
                        )

                        StatusBar(state)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBar(state: BrowserState) {
    Surface(
        tonalElevation = 1.dp
    ) {
        if (state.selected.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.selected.size == 1) {
                    state.selected.single().let { selectedFile ->
                        Text(
                            text = selectedFile.name,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.width(16.dp))

                        Text("Size: ${selectedFile.toFile().humanReadableSize()}")
                        Spacer(Modifier.weight(1f, true))
                        Text("${state.files.size} items")
                    }
                } else {
                    Text("${state.selected.size} items selected")
                }
            }
        }
    }
}

class Browser : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<BrowserScreenModel>()
    }
}