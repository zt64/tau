package dev.zt64.tau.ui.window

import Res
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import dev.zt64.tau.util.creationTime
import dev.zt64.tau.util.humanFriendly
import dev.zt64.tau.util.humanReadableSize
import dev.zt64.tau.util.rememberVectorPainter
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.text.contains

private enum class Tab(val label: String, val icon: ImageVector) {
    DETAILS(Res.string.details, Icons.Default.Info),
    PERMISSIONS(Res.string.permissions, Icons.Default.Lock)
}

@Composable
fun PropertiesWindow(
    path: Path,
    onCloseRequest: () -> Unit
) {
    val windowState = rememberWindowState(width = 450.dp, height = 400.dp)

    val icon = rememberVectorPainter(
        image = Icons.Default.Info,
        tint = if (isSystemInDarkTheme()) Color.White else Color.Black
    )

    Window(
        title = "${path.name} - ${Res.string.properties}",
        icon = icon,
        state = windowState,
        resizable = false,
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
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        when (tab) {
                            Tab.DETAILS -> DetailsTab(path)
                            Tab.PERMISSIONS -> PermissionsTab(path)
                        }
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
            Text(Res.string.name)
        },
        trailingContent = {
            Text(path.name)
        }
    )
    ListItem(
        headlineContent = {
            Text(Res.string.location)
        },
        trailingContent = {
            Text(path.pathString)
        }
    )
    ListItem(
        headlineContent = {
            Text(Res.string.size)
        },
        trailingContent = {
            Text(path.toFile().humanReadableSize())
        }
    )
    ListItem(
        headlineContent = {
            Text(Res.string.date_created)
        },
        trailingContent = {
            Text(path.creationTime().humanFriendly())
        }
    )
    ListItem(
        headlineContent = {
            Text(Res.string.date_modified)
        },
        trailingContent = {
            Text(path.getLastModifiedTime().toInstant().humanFriendly())
        }
    )

    if (!path.isRegularFile()) {
        ListItem(
            headlineContent = {
                Text(Res.string.contents)
            },
            trailingContent = {
                val entries = remember {
                    path.listDirectoryEntries()
                }

                Text("${entries.size} ${Res.string.items}")
            }
        )
    }
}

@Composable
private fun PermissionsTab(path: Path) {
    val isWindows = remember { System.getProperty("os.name").contains("Windows") }

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Person, Res.string.owner)
        },
        headlineContent = {
            Text(Res.string.owner)
        },
        trailingContent = {
            Text(path.getOwner()?.name ?: Res.string.unknown)
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, Res.string.owner_access)
        },
        headlineContent = {
            Text(Res.string.owner_access)
        },
        trailingContent = {
        }
    )

    HorizontalDivider(thickness = 1.dp)

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Group, Res.string.group)
        },
        headlineContent = {
            Text(Res.string.group)
        },
        trailingContent = {
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, Res.string.group_access)
        },
        headlineContent = {
            Text(Res.string.group_access)
        },
        trailingContent = {
        }
    )

    HorizontalDivider(thickness = 1.dp)

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Public, Res.string.global)
        },
        headlineContent = {
            Text(Res.string.global)
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, Res.string.global_access)
        },
        headlineContent = {
            Text(Res.string.global_access)
        },
        trailingContent = {
        }
    )

    if (!isWindows) {
        ListItem(
            headlineContent = { Text(Res.string.execute) },
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