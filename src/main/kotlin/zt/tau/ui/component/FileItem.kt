package zt.tau.ui.component

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import zt.tau.ui.window.PropertiesWindow
import zt.tau.util.setContents
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.File
import java.nio.file.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.name

@Composable
fun FileItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
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
        modifier = Modifier
            .fillMaxSize()
            .background(if (selected) MaterialTheme.colorScheme.secondary else Color.Unspecified)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val clipboardManager = LocalClipboardManager.current

        ContextMenuArea(
            items = {
                buildList {
                    add(ContextMenuItem("Copy") {
                        clipboardManager.setContents(FileTransferable(listOf(path.toFile())), null)
                    })
                    add(ContextMenuItem("Cut") {

                    })
                    add(ContextMenuItem("Properties") {
                        showProperties = true
                    })
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(52.dp),
                    imageVector = if (path.isRegularFile()) Icons.Default.Description else Icons.Default.Folder,
                    tint = LocalContentColor.current,
                    contentDescription = null
                )
                Text(path.name)
            }
        }
    }
}

data class FileTransferable(private val listOfFiles: List<File>) : Transferable {
    override fun getTransferDataFlavors() = arrayOf(DataFlavor.javaFileListFlavor)
    override fun isDataFlavorSupported(flavor: DataFlavor) = DataFlavor.javaFileListFlavor == flavor
    override fun getTransferData(flavor: DataFlavor) = listOfFiles
}