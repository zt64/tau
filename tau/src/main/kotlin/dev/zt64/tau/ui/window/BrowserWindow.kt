@file:OptIn(KoinExperimentalAPI::class)

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
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.component.*
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
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
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.name

@OptIn(ExperimentalComposeUiApi::class, ExperimentalSplitPaneApi::class)
@Composable
fun BrowserWindow() {
    val viewModel = koinViewModel<BrowserViewModel>()
    val preferencesManager = koinInject<PreferencesManager>()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = Modifier.onKeyEvent {
            if (it.type != KeyEventType.KeyUp) return@onKeyEvent false

            when {
                it.isCtrlPressed -> {
                    when (it.key) {
                        Key.C -> {
                            val selectedFiles = viewModel.selected
                            val files = selectedFiles.map(Path::toFile)

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
                        }
                        Key.R -> {
                            viewModel.refresh()
                        }
                        Key.H -> {
                            preferencesManager.showHiddenFiles = !preferencesManager.showHiddenFiles
                            viewModel.refresh()
                        }
                        Key.F -> {
                            // state.focusSearch()
                        }
                        Key.T -> {
                            viewModel.newTab()
                        }
                        Key.W -> {
                            viewModel.closeTab()
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
        snackbarHost = { SnackbarHost(viewModel.snackbarHostState) }
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

                    val currentLocation = viewModel.currentLocation.toFile()

                    dragData.readFiles().forEach { path ->
                        File(path).moveTo(currentLocation)
                    }
                }
        ) {
            val watcher = viewModel.watcher

            val coroutineScope = rememberCoroutineScope()

            DisposableEffect(viewModel.currentLocation) {
                coroutineScope.launch(Dispatchers.IO) {
                    watcher.add(viewModel.currentLocation.absolutePathString())
                    watcher.onEventFlow.collectLatest {
                        viewModel.refresh()
                    }
                }

                onDispose {
                    coroutineScope.launch {
                        watcher.removeAll()
                    }
                }
            }

            Toolbar()

            HorizontalSplitPane(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true),
                splitPaneState = rememberSplitPaneState(0.2f)
            ) {
                first(minSize = 120.dp) {
                    SidePanel()
                }

                second {
                    Column {
                        TabsRow()

                        FileGrid(
                            modifier = Modifier.weight(1f, true)
                        )

                        StatusBar()
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBar() {
    val viewModel = koinViewModel<BrowserViewModel>()

    Surface(
        tonalElevation = 1.dp
    ) {
        if (viewModel.selected.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.selected.size == 1) {
                    viewModel.selected.single().let { selectedFile ->
                        Text(
                            text = selectedFile.name,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.width(16.dp))

                        Text("Size: ${selectedFile.toFile().humanReadableSize()}")
                        Spacer(Modifier.weight(1f, true))
                        Text("${viewModel.files.size} items")
                    }
                } else {
                    Text("${viewModel.selected.size} items selected")
                }
            }
        }
    }
}