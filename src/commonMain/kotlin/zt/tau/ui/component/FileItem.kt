package zt.tau.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zt.tau.ui.window.PropertiesWindow
import zt.tau.ui.window.selectedFile
import zt.tau.util.setContents
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.File
import java.nio.file.Path
import kotlin.io.path.isReadable
import kotlin.io.path.isRegularFile
import kotlin.io.path.isSymbolicLink
import kotlin.io.path.name

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileItem(
    path: Path,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showProperties by remember { mutableStateOf(false) }

    if (showProperties) {
        PropertiesWindow(
            path = path,
            onCloseRequest = { showProperties = false }
        )
    }

    Column(
        modifier = Modifier
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick
            )
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val clipboardManager = LocalClipboardManager.current

        ContextMenuArea(
            items = {
                listOf(
                    ContextMenuItem("Copy") {
                        clipboardManager.setContents(FileTransferable(listOf(path.toFile())), null)
                    },
                    ContextMenuItem("Cut") {

                    },
                    ContextMenuItem("Delete") {

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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Icon(
                            modifier = Modifier.size(54.dp),
                            imageVector = if (path.isRegularFile()) Icons.Default.Description else Icons.Default.Folder,
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
                                    .size(28.dp)
                                    .align(Alignment.BottomEnd),
                                imageVector = icon,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        text = if (path.name.length > 15 && (selectedFile != path) ) {
                            path.name.take(15) + "..."
                        } else {
                            path.name
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            shadow = Shadow(
                                color = MaterialTheme.colorScheme.outline,
                                offset = Offset.Zero,
                                blurRadius = 0.75f
                            )
                        ),
                        textAlign = TextAlign.Center
                    )
                }
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