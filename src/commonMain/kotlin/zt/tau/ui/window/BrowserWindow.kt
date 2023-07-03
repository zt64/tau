package zt.tau.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import zt.tau.ui.component.FileItem
import zt.tau.ui.component.PathBar
import zt.tau.ui.component.SidePanel
import zt.tau.util.humanReadableSize
import java.awt.Desktop
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.*
import kotlin.io.path.*

var currentLocation by mutableStateOf(Path("/"))
var selectedFile by mutableStateOf(Path("/"))


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserWindow() {
    val snackbarData = remember { SnackbarHostState() }

    val watcher = remember {
        FileSystems.getDefault().newWatchService()
    }

    DisposableEffect(Unit) {
        onDispose(watcher::close)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarData)
        }
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
            val watchKey = currentLocation.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)

            coroutineScope.launch(Dispatchers.IO) {
                do {
                    val key = try {
                        watcher.take()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()

                        break
                    }

                    key.pollEvents()

                    scanDir()

                    key.reset()
                } while (key.isValid)
            }

            onDispose {
                watchKey.cancel()
            }
        }

        LaunchedEffect(currentLocation, search) {
            scanDir()
        }

        Column {
            Surface(
                tonalElevation = 5.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        enabled = currentLocation.parent != null,
                        onClick = { currentLocation = currentLocation.parent }
                    ) {
                        Icon(Icons.Default.ArrowUpward, null)
                    }

                    FilledIconButton(
                        enabled = false,
                        onClick = {

                        }
                    ) {
                        Icon(Icons.Default.ArrowLeft, null)
                    }

                    FilledIconButton(
                        enabled = false,
                        onClick = {

                        }
                    ) {
                        Icon(Icons.Default.ArrowRight, null)
                    }

                    PathBar(
                        modifier = Modifier.weight(1f, true),
                        location = currentLocation,
                        onClickSegment = { path -> currentLocation = path }
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
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                SidePanel()

                Column {
                    LazyVerticalGrid(
                        modifier = Modifier.weight(1f, true),
                        columns = GridCells.Adaptive(78.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(12.dp),
                    ) {
                        items(
                            items = files,
                            key = { it.name }
                        ) { path ->
                            FileItem(
                                path = path,
                                onClick = {
                                    selectedFile = path
                                },
                                onDoubleClick = {
                                    when {
                                        path.isDirectory() -> {
                                            if (path.isReadable()) currentLocation = path
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
                                modifier = Modifier
                                    .background(
                                        if (path.toAbsolutePath() == selectedFile.toAbsolutePath()) {
                                            MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.25f
                                            )
                                        } else {
                                            MaterialTheme.colorScheme.surface
                                        }
                                    )
                            )
                        }
                    }

                    Surface(
                        tonalElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .height(24.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            selectedFile.fileName?.let {
                                Text(
                                    text = it.nameWithoutExtension,
                                    fontWeight = FontWeight.Bold,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.width(16.dp))
                            }

                            Text("Size: " + selectedFile.toFile().humanReadableSize())
                            Spacer(Modifier.weight(1f, true))
                            Text("${files.count()} items")
                        }
                    }
                }
            }
        }
    }
}