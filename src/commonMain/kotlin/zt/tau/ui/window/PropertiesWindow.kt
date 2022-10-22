package zt.tau.ui.window

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import zt.tau.util.rememberVectorPainter
import java.nio.file.Path
import kotlin.io.path.*

private enum class Tab(
    val label: String,
    val icon: ImageVector
) {
    DETAILS("Details", Icons.Default.Info),
    PERMISSIONS("Permissions", Icons.Default.Lock)
}

@OptIn(ExperimentalAnimationApi::class)
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
                var selectedTab by rememberSaveable { mutableStateOf(Tab.DETAILS) }

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

                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) { tab ->
                    when (tab) {
                        Tab.DETAILS -> DetailsTab(path)
                        Tab.PERMISSIONS -> PermissionsTab(path)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTab(path: Path) {
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
            Text("${path.toFile().humanReadableSize()")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PermissionsTab(path: Path) {
    val isWindows = remember { System.getProperty("os.name").contains("Windows") }

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Person, "Owner")
        },
        headlineText = {
            Text("Owner")
        },
        trailingContent = {
            Text(path.getOwner()?.name ?: "Unknown")
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, "Owner access")
        },
        headlineText = {
            Text("Owner access")
        },
        trailingContent = {

        }
    )

    Divider(thickness = 1.dp)

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Group, "Group")
        },
        headlineText = {
            Text("Group")
        },
        trailingContent = {

        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, "Group access")
        },
        headlineText = {
            Text("Group access")
        },
        trailingContent = {

        }
    )

    Divider(thickness = 1.dp)

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Public, "Global")
        },
        headlineText = {
            Text("Global")
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, "Global access")
        },
        headlineText = {
            Text("Global access")
        },
        trailingContent = {

        }
    )

    if (!isWindows) {
        ListItem(
            headlineText = { Text("Execute") },
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