package zt.tau.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.tika.Tika
import zt.tau.ui.window.PropertiesWindow
import zt.tau.util.contains
import zt.tau.util.setContents
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.File
import java.nio.file.Path
import kotlin.io.path.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileItem(
    path: Path,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    var showProperties by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    if (showProperties) {
        PropertiesWindow(
            path = path,
            onCloseRequest = { showProperties = false }
        )
    }

    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItem("Copy") {
                    clipboardManager.setContents(FileTransferable(listOf(path.toFile())), null)
                },
                ContextMenuItem("Cut") {

                },
                ContextMenuItem("Delete") {
                    path.deleteIfExists()
                },
                ContextMenuItem("Properties") {
                    showProperties = true
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        onClick = onClick,
                        onDoubleClick = onDoubleClick
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
                            if (path.isDirectory()) Icons.Default.Folder else Icons.Default.Description
                        )
                    }

                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            if (path.isDirectory()) return@launch

                            val (dataType, dataFormat) = runCatching {
                                Tika()
                                    .detect(path.inputStream(), path.fileName.toString())
                                    .split("/")
                            }.getOrNull() ?: return@launch

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
                        modifier = Modifier.fillMaxSize(),
                        imageVector = fileIcon,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )

                    val icon = when {
                        path.isSymbolicLink() -> Icons.Default.Link
                        !path.isReadable() -> Icons.Default.Lock
                        else -> null
                    }

                    if (icon != null) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize(0.3f)
                                .align(Alignment.BottomEnd),
                            imageVector = icon,
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = null
                        )
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
        onClick = {},
        onDoubleClick = {}
    )
}

data class FileTransferable(private val listOfFiles: List<File>) : Transferable {
    override fun getTransferDataFlavors() = arrayOf(DataFlavor.javaFileListFlavor)
    override fun isDataFlavorSupported(flavor: DataFlavor) = DataFlavor.javaFileListFlavor == flavor
    override fun getTransferData(flavor: DataFlavor) = listOfFiles
}