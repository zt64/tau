package dev.zt64.tau.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.tau.R
import dev.zt64.tau.util.contains
import dev.zt64.tau.util.copyToClipboard
import dev.zt64.ui.window.PropertiesWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.tika.Tika
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.File
import java.nio.file.Path
import kotlin.io.path.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FileItem(
    path: Path,
    selected: Boolean,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    var showProperties by remember { mutableStateOf(false) }

    if (showProperties) {
        PropertiesWindow(
            path = path,
            onCloseRequest = { showProperties = false }
        )
    }

    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItem(R.strings.COPY) {
                    listOf(path.toFile()).copyToClipboard()
                },
                ContextMenuItem(R.strings.CUT) {
                },
                ContextMenuItem(R.strings.DELETE) {
                    try {
                        path.deleteIfExists()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                ContextMenuItem(R.strings.PROPERTIES) {
                    showProperties = true
                },
                ContextMenuItem(R.strings.OPEN_IN_NEW_TAB) {

                },
            )
        }
    ) {
        TooltipArea(
            tooltip = {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 6.dp,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = path.name
                    )
                }
            }
        ) {
            var selected2 by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        onClick = {
                            selected2 = !selected2
                        },
                        onDoubleClick = onDoubleClick
                    )
                    .semantics {
                        this.selected = selected2
                    }
                    .background(
                        if (selected2) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                    )
                    .then(modifier),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.padding(8.dp)
                ) {
                    val coroutineScope = rememberCoroutineScope { Dispatchers.IO }
                    var fileIcon by remember {
                        mutableStateOf(
                            if (path.isDirectory()) {
                                Icons.Default.Folder
                            } else {
                                Icons.Default.Description
                            }
                        )
                    }

                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            if (path.isDirectory()) return@launch

                            val (dataType, dataFormat) = runCatching {
                                Tika()
                                    .detect(path.inputStream(), path.name)
                                    .split("/")
                            }.getOrElse { return@launch }

                            fileIcon = when (dataType) {
                                "image" -> Icons.Default.Image
                                "video" -> Icons.Default.VideoFile
                                "audio" -> Icons.Default.AudioFile
                                "text" -> Icons.Default.Article
                                "font" -> Icons.Default.TextFields
                                "application" -> when {
                                    dataFormat.contains("zip", "7z", "rar", "tar") -> Icons.Default.Archive
                                    dataFormat == "java-archive" -> Icons.Default.Coffee
                                    dataFormat == "ogg" -> Icons.Default.MusicVideo

                                    else -> Icons.Default.Description
                                }

                                else -> Icons.Default.Description
                            }
                        }
                    }

                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = fileIcon,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )

                    val icon = when {
                        path.isSymbolicLink() -> Icons.Outlined.Link
                        !path.isReadable() -> Icons.Outlined.Lock
                        else -> null
                    }

                    if (icon != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize(0.3f)
                                .align(Alignment.BottomEnd),
                            color = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null
                            )
                        }
                    }
                }

                Text(
                    text = path.name,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    style = MaterialTheme.typography.labelMedium.copy(
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.outline,
                            offset = Offset.Zero,
                            blurRadius = 0.75f
                        )
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun FileItemPreview() {
    FileItem(
        path = File("/home/tau").toPath(),
        selected = false,
        onClick = {},
        onDoubleClick = {}
    )
}

data class FileTransferable(private val listOfFiles: List<File>) : Transferable {
    override fun getTransferDataFlavors() = arrayOf(DataFlavor.javaFileListFlavor)
    override fun isDataFlavorSupported(flavor: DataFlavor) = DataFlavor.javaFileListFlavor == flavor
    override fun getTransferData(flavor: DataFlavor) = listOfFiles
}
