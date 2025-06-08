package dev.zt64.tau.ui.window

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import dev.zt64.tau.domain.manager.NotificationManager
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.model.ViewMode
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.window_icon
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import dev.zt64.tau.ui.widget.*
import dev.zt64.tau.ui.widget.browse.BrowseView
import dev.zt64.tau.ui.widget.browse.DetailList
import dev.zt64.tau.ui.widget.browse.FileVerticalGrid
import dev.zt64.tau.ui.widget.toolbar.Toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import java.awt.Dimension
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.moveTo
import kotlin.system.exitProcess

@Composable
fun BrowserWindow(
    onClickShowPreferences: () -> Unit,
    onCloseRequest: () -> Unit
) {
    val preferencesManager = koinInject<PreferencesManager>()
    Window(
        title = "tau",
        icon = painterResource(Res.drawable.window_icon),
        onCloseRequest = onCloseRequest
        // onKeyEvent = {
        //     if (it.type == KeyEventType.KeyDown) {
        //         when (it.key) {
        //             Key.AltLeft -> showMenuBar = !showMenuBar
        //             else -> return@Window false
        //         }
        //
        //         return@Window true
        //     }
        //
        //     false
        // }
    ) {
        LaunchedEffect(Unit) {
            window.minimumSize = Dimension(300, 400)
        }

        Column {
            if (preferencesManager.showMenuBar) {
                MenuBar(
                    onClickPreferences = onClickShowPreferences,
                )
            }

            BrowserWindowContent()
        }
    }
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun BrowserWindowContent(
    showToolbar: Boolean = true,
    showStatusBar: Boolean = true,
) {
    val viewModel = koinViewModel<BrowserViewModel>()
    val notificationManager = koinInject<NotificationManager>()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        notificationManager.events.collect { event ->
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action?.label
                )

                if (result == SnackbarResult.ActionPerformed) {
                    event.action?.action?.invoke()
                }
            }
        }
    }

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
                        Key.H -> viewModel.toggleHiddenFiles()
                        Key.F -> {
                            viewModel.searching = true
                        }
                        Key.T -> viewModel.newTab()
                        Key.W -> viewModel.closeTab()
                        Key.N -> {
                            // Open new window
                        }
                        Key.Q -> exitProcess(0)
                        else -> return@onPreviewKeyEvent false
                    }
                }
                else -> return@onPreviewKeyEvent false
            }

            true
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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

            if (showToolbar) Toolbar()

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
                                value = viewModel.searchQuery,
                                onValueChange = viewModel::search
                            )
                        }

                        BrowseView(
                            modifier = Modifier.weight(1f),
                            onClick = viewModel::clearSelection
                        ) {
                            when (viewModel.viewMode) {
                                ViewMode.LIST -> DetailList()
                                ViewMode.GRID -> FileVerticalGrid()
                            }
                        }

                        if (showStatusBar) StatusBar()
                    }
                }
            }
        }
    }
}