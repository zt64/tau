package zt.tau.ui.window

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.irgaly.kfswatch.KfsDirectoryWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import zt.tau.ui.component.FileItem
import zt.tau.ui.component.PathBar
import zt.tau.ui.component.SidePanel
import zt.tau.util.humanReadableSize
import java.awt.Desktop
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.*

var currentLocation by mutableStateOf(Path("/"), referentialEqualityPolicy())
var selectedFile by mutableStateOf(Path("/"), referentialEqualityPolicy())

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BrowserWindow() {
    val snackbarData = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    val watcher = remember { KfsDirectoryWatcher(scope) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarData) },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            val coroutineScope = rememberCoroutineScope()

            var files by remember { mutableStateOf(listOf<Path>()) }
            var search by remember { mutableStateOf("") }

            // TODO: Move this to business logic
            fun scanDir() {
                files = try {
                    currentLocation.listDirectoryEntries()
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

            DisposableEffect(currentLocation) {
                coroutineScope.launch(Dispatchers.IO) {
                    watcher.add(currentLocation.absolutePathString())
                    watcher.onEventFlow.collectLatest {
                        scanDir()
                    }
                }

                onDispose {
                    coroutineScope.launch {
                        watcher.removeAll()
                    }
                }
            }

            LaunchedEffect(currentLocation, search) {
                scanDir()
            }

            Surface(
                tonalElevation = 7.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilledIconButton(
                        enabled = currentLocation.parent != null,
                        onClick = { currentLocation = currentLocation.parent },
                    ) {
                        Icon(Icons.Default.ArrowUpward, null)
                    }

                    FilledTonalIconButton(
                        enabled = false,
                        onClick = {
                        },
                    ) {
                        Icon(Icons.Default.ArrowLeft, null)
                    }

                    FilledTonalIconButton(
                        enabled = false,
                        onClick = {
                        },
                    ) {
                        Icon(Icons.Default.ArrowRight, null)
                    }

                    PathBar(
                        modifier = Modifier.weight(1f, true),
                        location = currentLocation,
                        onClickSegment = { path -> currentLocation = path },
                    )

                    TextField(
                        modifier = Modifier.heightIn(min = 42.dp, max = 42.dp),
                        value = search,
                        textStyle = MaterialTheme.typography.bodySmall,
                        onValueChange = { search = it },
                        trailingIcon = {
                            Icon(Icons.Default.Search, null)
                        },
                        singleLine = true,
                        shape = CircleShape,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                SidePanel()

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val tabs = remember { mutableStateListOf(currentLocation) }
                    var selectedTab by remember { mutableStateOf(0) }
                    var scale by remember { mutableStateOf(78.dp) }

                    AnimatedVisibility(
                        visible = tabs.size > 1,
                        enter = expandVertically { it },
                        exit = shrinkVertically { it },
                    ) {
                        ScrollableTabRow(
                            modifier = Modifier.fillMaxWidth(),
                            selectedTabIndex = selectedTab,
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                            edgePadding = 0.dp,
                        ) {
                            tabs.forEachIndexed { index, title ->
                                val interactionSource = remember { MutableInteractionSource() }

                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    interactionSource = interactionSource,
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .height(40.dp)
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        val isHovered by interactionSource.collectIsHoveredAsState()

                                        Text(
                                            text = title.pathString,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.width(60.dp),
                                        )

                                        IconButton(
                                            onClick = { tabs.removeAt(index) },
                                            modifier = Modifier.width(20.dp),
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(16.dp),
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    LazyVerticalGrid(
                        modifier = Modifier
                            .weight(1f, true)
                            .onPointerEvent(
                                eventType = PointerEventType.Scroll,
                                pass = PointerEventPass.Initial,
                            ) { ev ->
                                if (!ev.keyboardModifiers.isCtrlPressed) return@onPointerEvent

                                scale += ev.changes.first().scrollDelta.y.dp
                            },
                        columns = GridCells.FixedSize(scale),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(12.dp),
                    ) {
                        items(
                            items = files,
                            key = { it.name },
                        ) { path ->
                            FileItem(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        if (path == selectedFile) {
                                            MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.25f,
                                            )
                                        } else {
                                            MaterialTheme.colorScheme.surface
                                        },
                                    ),
                                path = path,
                                onClick = { selectedFile = path },
                                onDoubleClick = {
                                    when {
                                        path.isDirectory() -> {
                                            if (path.isReadable()) {
                                                currentLocation = path
                                            } else {
                                                coroutineScope.launch {
                                                    snackbarData.showSnackbar("No permission to read ${path.name}")
                                                }
                                            }
                                        }

                                        path.isRegularFile() -> {
                                            try {
                                                Desktop.getDesktop().open(path.toFile())
                                            } catch (e: IOException) {
                                                e.printStackTrace()

                                                coroutineScope.launch {
                                                    snackbarData.showSnackbar("No default application for ${path.name}")
                                                }
                                            }
                                        }

                                        path.isExecutable() -> {
                                            Desktop.getDesktop().open(path.toFile())
                                        }
                                    }
                                },
                            )
                        }
                    }

                    Surface(
                        tonalElevation = 1.dp,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .height(24.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            selectedFile.fileName?.let {
                                Text(
                                    text = it.name,
                                    fontWeight = FontWeight.Bold,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Spacer(Modifier.width(16.dp))
                            }

                            Text("Size: ${selectedFile.toFile().humanReadableSize()}")
                            Spacer(Modifier.weight(1f, true))
                            Text("${files.size} items")
                        }
                    }
                }
            }
        }
    }
}
