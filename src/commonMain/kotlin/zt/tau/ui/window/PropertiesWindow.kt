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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import zt.tau.util.rememberVectorPainter
import java.nio.file.Path
import kotlin.io.path.*

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
        icon = rememberVectorPainter(
            image = Icons.Default.Info,
            tint = Color.White
        ),
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
                        Column {
                            ListItem(
                                headlineText = {
                                    Text("Name")
                                },
                                trailingContent = {
                                    Text(path.name)
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
                                    Text("${path.fileSize()} bytes")
                                }
                            )
                        }
                    }
                    Tab.PERMISSIONS -> {
                        Column {
                            ListItem(
                                headlineText = {
                                    Text("Owner")
                                },
                                trailingContent = {
                                    Text(path.getOwner()?.name ?: "Unknown")
                                }
                            )

                            ListItem(
                                headlineText = {
                                    Text("Access")
                                },
                                trailingContent = {

                                }
                            )

                            ListItem(
                                headlineText = {
                                    Text("Group")
                                },
                                trailingContent = {

                                }
                            )

                            ListItem(
                                headlineText = {
                                    Text("Access")
                                },
                                trailingContent = {

                                }
                            )

                            ListItem(
                                headlineText = {
                                    Text("Access")
                                },
                                trailingContent = {

                                }
                            )

                            if (!System.getProperty("os.name").contains("Windows")) {
                                ListItem(
                                        headlineText = {
                                            Text("Execute")
                                        },
                                        trailingContent = {
                                            Switch(
                                                    checked = path.isExecutable(),
                                                    onCheckedChange = {

                                                    }
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}