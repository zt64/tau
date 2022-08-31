package zt.tau.ui.window

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import zt.tau.enum.Sort
import zt.tau.ui.component.FileItem
import zt.tau.ui.component.PathBar
import zt.tau.ui.component.SidePanel
import java.io.IOException
import kotlin.io.path.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserWindow() {
    var isOpen by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf(Path("/")) }
    var search by remember { mutableStateOf("*") }
    val sort by remember { mutableStateOf(Sort.NAME) }
    val files = remember(currentLocation, search, sort) {
        try {
            currentLocation.listDirectoryEntries(search).sortedWith(compareBy {
                when (sort) {
                    Sort.NAME -> it.name.lowercase()
                    Sort.SIZE -> it.fileSize()
                    Sort.EXT -> it.extension
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    if (isOpen) {
        ConfigWindow(
            onCloseRequest = { isOpen = false }
        )
    }

    Surface {
        Column {
            Surface(
                tonalElevation = 5.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                        onClick = {

                        }
                    ) {
                        Icon(Icons.Default.ArrowLeft, null)
                    }

                    FilledIconButton(
                        onClick = {

                        }
                    ) {
                        Icon(Icons.Default.ArrowRight, null)
                    }

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

                    PathBar(
                        currentLocation = currentLocation
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
                    var selectedIndex by remember { mutableStateOf(-1) }

                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, true)
                            .selectableGroup(),
                        columns = GridCells.Adaptive(78.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        itemsIndexed(files) { index, path ->
                            FileItem(
                                modifier = Modifier
                                    .selectable(
                                        selected = selectedIndex == index,
                                        onClick = { selectedIndex = index }
                                    )
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onDoubleTap = {
                                                when {
                                                    path.isDirectory() -> currentLocation = path
                                                    path.isRegularFile() -> {

                                                    }

                                                    path.isExecutable() -> {

                                                    }
                                                }
                                            }
                                        )
                                        detectDragGestures(
                                            onDrag = { pointerInputChange: PointerInputChange, offset: Offset ->

                                            }
                                        )
                                    },
                                selected = selectedIndex == index,
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
                            Text("Information about current files selected")
                        }
                    }
                }
            }
        }
    }
}