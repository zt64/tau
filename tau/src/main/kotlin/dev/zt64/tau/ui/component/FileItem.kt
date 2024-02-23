package dev.zt64.tau.ui.component

import Res
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.window.PropertiesWindow
import dev.zt64.tau.util.contains
import dev.zt64.tau.util.setContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.tika.Tika
import org.koin.compose.koinInject
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.File
import java.nio.file.Path
import kotlin.io.path.*

@OptIn(ExperimentalFoundationApi::class)
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
    val preferencesManager = koinInject<PreferencesManager>()

    if (showProperties) {
        PropertiesWindow(
            path = path,
            onCloseRequest = { showProperties = false }
        )
    }

    val clipboardManager = LocalClipboardManager.current

    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItem(Res.string.copy) {
                    clipboardManager.setContents(FileTransferable(listOf(path.toFile())), null)
                },
                ContextMenuItem(Res.string.cut) {
                },
                ContextMenuItem(Res.string.delete) {
                    try {
                        path.deleteIfExists()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                ContextMenuItem(Res.string.properties) {
                    showProperties = true
                },
                ContextMenuItem(Res.string.open_in_tab) {
                }
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
                        onClick = { selected2 = !selected2 },
                        onDoubleClick = onDoubleClick
                    ).semantics { this.selected = selected2 }
                    .background(
                        if (selected2) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ).then(modifier),
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
                                "text" -> Icons.AutoMirrored.Filled.Article
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

                var fileName by remember(path) {
                    mutableStateOf(path.name)
                }

                var renaming by remember { mutableStateOf(true) }

                BasicTextField(
                    modifier = Modifier,
                    readOnly = !renaming,
                    value = fileName,
                    // textAlign = TextAlign.Center,
                    // overflow = if (preferencesManager.truncateNames) {
                    //     TextOverflow.Ellipsis
                    // } else {
                    //     TextOverflow.Visible
                    // },
                    maxLines = if (preferencesManager.truncateNames && !renaming) {
                        preferencesManager.maxNameLines
                    } else {
                        Int.MAX_VALUE
                    },
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = LocalContentColor.current,
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.outline,
                            offset = Offset.Zero,
                            blurRadius = 0.75f
                        ),
                        textAlign = TextAlign.Center
                    ),
                    onValueChange = {
                        // ensure that the file name is valid
                        if (it.contains(Regex("[\\\\/:*?\"<>|]"))) {
                            fileName = it
                        }
                    }
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

data class FileTransferable(
    private val listOfFiles: List<File>
) : Transferable {
    override fun getTransferDataFlavors() = arrayOf(DataFlavor.javaFileListFlavor)

    override fun isDataFlavorSupported(flavor: DataFlavor) = DataFlavor.javaFileListFlavor == flavor

    override fun getTransferData(flavor: DataFlavor) = listOfFiles
}