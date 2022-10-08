package zt.tau.ui.window

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import zt.tau.ui.component.FileItem
import zt.tau.ui.component.PathBar
import zt.tau.ui.component.SidePanel
import zt.tau.ui.component.TextField
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.*

var currentLocation by mutableStateOf(Path("/"))

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun BrowserWindow() {
    Surface {
        var search by remember { mutableStateOf("") }
        val files = remember(currentLocation, search) {
            try {
                currentLocation.listDirectoryEntries()
                    .filter { it.name.contains(search) }
                    .sortedBy { it.nameWithoutExtension }
                    .sortedBy { it.isRegularFile() }
                    .sortedBy { it.startsWith(".") }
            } catch (e: IOException) {
                e.printStackTrace()
                emptyList()
            }
        }

        Column {
            Surface(
                tonalElevation = 5.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        enabled = currentLocation.parent != null,
                        onClick = {
                            currentLocation = currentLocation.parent
                        }
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

                    PathBar(currentLocation)

                    Spacer(Modifier.weight(1f, true))

                    TextField(
                        value = search,
                        trailingIcon = {
                            Icon(Icons.Default.Search, null)
                        },
                        onValueChange = {
                            search = it
                        },
                        singleLine = true
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
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(
                            items = files,
                            key = Path::hashCode
                        ) { path ->
                            FileItem(
                                modifier = Modifier
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onDoubleTap = {
                                                when {
                                                    path.isDirectory() -> {
                                                        if (path.isReadable()) currentLocation = path
                                                    }
                                                    path.isRegularFile() -> {

                                                    }
                                                    path.isExecutable() -> {

                                                    }
                                                }
                                            }
                                        )
                                    },
                                path = path
                            )
                        }
                    }

                    Surface(
                        tonalElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text("${files.count()} items")
                        }
                    }
                }
            }
        }
    }
}