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
import kotlin.io.path.absolutePathString
import kotlin.io.path.name

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun BrowserWindow() {
    val viewModel = koinViewModel<BrowserViewModel>()
    val preferencesManager = koinInject<PreferencesManager>()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = Modifier.onPreviewKeyEvent {
            if (it.type != KeyEventType.KeyUp) return@onPreviewKeyEvent false

            when {
                it.isCtrlPressed -> {
                    when (it.key) {
                        Key.C -> viewModel.copy()
                        Key.V -> viewModel.paste()
                        Key.X -> {
                            // Cut
                        }
                        Key.A -> viewModel.selectAll()
                        Key.R -> viewModel.refresh()
                        Key.H -> {
                            preferencesManager.showHiddenFiles = !preferencesManager.showHiddenFiles
                            viewModel.refresh()
                        }
                        Key.F -> {
                            // state.focusSearch()
                        }
                        Key.T -> viewModel.newTab()
                        Key.W -> viewModel.closeTab()
                        Key.N -> {
                            // Open new window
                        }
                        else -> return@onPreviewKeyEvent false
                    }
                }
                it.key == Key.Enter -> {
                    // Open file
                }
                else -> return@onPreviewKeyEvent false
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
                modifier = Modifier.fillMaxWidth(),
                splitPaneState = rememberSplitPaneState(0.2f)
            ) {
                first(minSize = 120.dp) {
                    SidePanel()
                }

                second {
                    Column {
                        TabRow()

                        FileVerticalGrid(
                            modifier = Modifier.weight(1f)
                        )

                        StatusBar()
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBar(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<BrowserViewModel>()

    Surface(
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (viewModel.selected.size) {
                0 -> {
                    Text("${viewModel.contents.size} items")
                }
                1 -> {
                    viewModel.selected.single().let { selectedFile ->
                        Text(
                            text = selectedFile.name,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.width(16.dp))

                        Text("Size: ${selectedFile.toFile().humanReadableSize()}")
                    }
                }
                else -> {
                    Text("${viewModel.selected.size} items selected")
                }
            }
        }
    }
}