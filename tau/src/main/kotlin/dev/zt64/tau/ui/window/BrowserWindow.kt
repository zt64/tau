package dev.zt64.tau.ui.window

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import dev.zt64.tau.ui.widget.*
import dev.zt64.tau.ui.widget.browse.DetailList
import dev.zt64.tau.ui.widget.toolbar.Toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.moveTo

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
                            viewModel.searching = true
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
        val currentLocation by viewModel.nav.currentLocation.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = {
                        it.dragData() is DragData.FilesList
                    },
                    target = object : DragAndDropTarget {
                        override fun onDrop(event: DragAndDropEvent): Boolean {
                            val data = event.dragData() as DragData.FilesList

                            data.readFiles().forEach { path ->
                                Path(path).moveTo(currentLocation)
                            }

                            return true
                        }
                    }
                )
        ) {
            val watcher = viewModel.watcher

            val coroutineScope = rememberCoroutineScope()

            DisposableEffect(currentLocation) {
                coroutineScope.launch(Dispatchers.IO) {
                    watcher.add(currentLocation.absolutePathString())
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

            if (preferencesManager.showToolbar) {
                Toolbar()
            }

            HorizontalSplitPane(
                modifier = Modifier.fillMaxWidth(),
                splitPaneState = rememberSplitPaneState(0.2f)
            ) {
                first(minSize = 120.dp) {
                    SidePanel()
                }

                second(minSize = 200.dp) {
                    Column {
                        TabRow()

                        if (viewModel.searching) {
                            SearchBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 42.dp, max = 42.dp),
                                value = viewModel.search,
                                onValueChange = viewModel::search
                            )
                        }

                        // FileVerticalGrid(
                        //     modifier = Modifier.weight(1f)
                        // )

                        DetailList(
                            modifier = Modifier.weight(1f)
                        )

                        if (preferencesManager.showStatusBar) {
                            StatusBar()
                        }
                    }
                }
            }
        }
    }
}