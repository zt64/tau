package zt.tau.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import zt.tau.ui.window.PropertiesWindow
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
    modifier: Modifier = Modifier,
    path: Path
) {
    var showProperties by remember { mutableStateOf(false) }

    if (showProperties) {
        PropertiesWindow(
            path = path,
            onCloseRequest = { showProperties = false }
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val clipboardManager = LocalClipboardManager.current

        CompositionLocalProvider(
            LocalContextMenuRepresentation provides DefaultContextMenuRepresentation(
                backgroundColor = MaterialTheme.colorScheme.background,
                textColor = MaterialTheme.colorScheme.onBackground,
                itemHoverColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
            )
        ) {
            ContextMenuArea(
                items = {
                    listOf(
                        ContextMenuItem("Copy") {
                            clipboardManager.setContents(FileTransferable(listOf(path.toFile())), null)
                        },
                        ContextMenuItem("Cut") {

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
                                        .size(36.dp)
                                        .align(Alignment.BottomEnd),
                                    imageVector = icon,
                                    tint = MaterialTheme.colorScheme.inversePrimary,
                                    contentDescription = null
                                )
                            }
                        }

                        Text(path.name)
                    }
                }
            }
        }
    }
}

data class FileTransferable(private val listOfFiles: List<File>) : Transferable {
    override fun getTransferDataFlavors() = arrayOf(DataFlavor.javaFileListFlavor)
    override fun isDataFlavorSupported(flavor: DataFlavor) = DataFlavor.javaFileListFlavor == flavor
    override fun getTransferData(flavor: DataFlavor) = listOfFiles
}