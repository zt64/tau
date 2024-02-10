package dev.zt64.tau.ui.window

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.input.pointer.*
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

    Scaffold(
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
                        var scale by rememberSaveable { mutableStateOf(preferencesManager.scale) }

                        TabsRow(state)

                        Box(
                            modifier = Modifier.weight(1f, true)
                        ) {
                            val gridState = rememberLazyGridState()
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .onPointerEvent(
                                        eventType = PointerEventType.Scroll,
                                        pass = PointerEventPass.Initial
                                    ) { ev ->
                                        if (!ev.keyboardModifiers.isCtrlPressed) return@onPointerEvent

                                        scale += ev
                                            .changes
                                            .first()
                                            .scrollDelta
                                            .y
                                            .toInt()
                                        if (scale <= 2) {
                                            scale = 2
                                        }
                                        preferencesManager.scale = scale
                                    }.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = state::clickGrid
                                    ),
                                state = gridState,
                                columns = GridCells.FixedSize(scale.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(12.dp)
                            ) {
                                items(
                                    items = state.files,
                                    key = { it.name }
                                ) { path ->
                                    FileItem(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                if (path == state.selectedFile) {
                                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                                } else {
                                                    MaterialTheme.colorScheme.surface
                                                }
                                            ),
                                        selected = path == state.selectedFile,
                                        path = path,
                                        onClick = { state.selectedFile = path },
                                        onDoubleClick = { state.doubleClick(path) }
                                    )
                                }
                            }

                            VerticalScrollbar(
                                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                                adapter = rememberScrollbarAdapter(gridState)
                            )
                        }

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
        state.selectedFile?.let { selectedFile ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
        }
    }
}

class Browser : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<BrowserScreenModel>()
    }
}