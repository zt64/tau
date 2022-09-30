package zt.tau.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.name
import kotlin.io.path.pathString

enum class Tab(
    val label: String,
    val icon: ImageVector
) {
    DETAILS("Details", Icons.Default.Info),
    PERMISSIONS("Permissions", Icons.Default.Lock)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesWindow(
    path: Path,
    onCloseRequest: () -> Unit
) {
    val windowState = rememberWindowState(width = 300.dp, height = 400.dp)

    Window(
        title = "${path.name} - Properties",
        icon = rememberVectorPainter(Icons.Default.Info),
        state = windowState,
        onCloseRequest = onCloseRequest
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                var selectedTab by remember { mutableStateOf(Tab.DETAILS) }

                TabRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTabIndex = selectedTab.ordinal
                ) {
                    Tab.values().forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            text = { Text(tab.label) },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label
                                )
                            },
                            onClick = { selectedTab = tab }
                        )
                    }
                }

                when (selectedTab) {
                    Tab.DETAILS -> {
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
                    Tab.PERMISSIONS -> {

                    }
                }
            }
        }
    }
}