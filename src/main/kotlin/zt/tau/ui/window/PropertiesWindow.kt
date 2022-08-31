package zt.tau.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.name
import kotlin.io.path.pathString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesWindow(
    path: Path,
    onCloseRequest: () -> Unit
) {
    Window(
        title = "${path.name} - Properties",
        icon = rememberVectorPainter(Icons.Default.Info),
        onCloseRequest = onCloseRequest
    ) {
        var pathName by remember { mutableStateOf(path.name) }

        Column {
            ListItem(
                headlineText = {
                    Text("Name")
                },
                trailingContent = {
                    TextField(
                        value = pathName,
                        onValueChange = { newName ->
                            pathName = newName
                        }
                    )
                }
            )
            ListItem(
                headlineText = {
                    Text("Location")
                },
                trailingContent = {
                    Text(path.pathString)
                }
            )
            ListItem(
                headlineText = {
                    Text("Size")
                },
                trailingContent = {
                    Text(path.fileSize().toString())
                }
            )
        }
    }
}