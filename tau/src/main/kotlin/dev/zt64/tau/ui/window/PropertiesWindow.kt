package dev.zt64.tau.ui.window

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
import dev.zt64.tau.R
import dev.zt64.tau.util.creationTime
import dev.zt64.tau.util.humanFriendly
import dev.zt64.tau.util.humanReadableSize
import dev.zt64.tau.util.rememberVectorPainter
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.text.contains

private enum class Tab(
    val label: String,
    val icon: ImageVector
) {
    DETAILS(R.strings.DETAILS, Icons.Default.Info),
    PERMISSIONS(R.strings.PERMISSIONS, Icons.Default.Lock)
}

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
            // TODO: respect dark mode
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
                    Tab.entries.forEach { tab ->
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
                        fadeIn() togetherWith fadeOut()
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

@Composable
private fun DetailsTab(path: Path) {
    ListItem( // TODO: Why are the first 3 items not showing up?
        headlineContent = {
            Text("Name")
        },
        trailingContent = {
            Text(path.name)
        }
    )
    ListItem(
        headlineContent = {
            Text("Location")
        },
        trailingContent = {
            Text(path.pathString)
        }
    )
    ListItem(
        headlineContent = {
            Text("Size")
        },
        trailingContent = {
            Text(path.toFile().humanReadableSize())
        }
    )
    ListItem(
        headlineContent = {
            Text("Created at")
        },
        trailingContent = {
            Text(path.creationTime().humanFriendly())
        }
    )
}

@Composable
private fun PermissionsTab(path: Path) {
    val isWindows = remember { System.getProperty("os.name").contains("Windows") }

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Person, "Owner")
        },
        headlineContent = {
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
        headlineContent = {
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
        headlineContent = {
            Text("Group")
        },
        trailingContent = {
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, "Group access")
        },
        headlineContent = {
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
        headlineContent = {
            Text("Global")
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, "Global access")
        },
        headlineContent = {
            Text("Global access")
        },
        trailingContent = {
        }
    )

    if (!isWindows) {
        ListItem(
            headlineContent = { Text("Execute") },
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