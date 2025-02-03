package dev.zt64.tau.ui.window

import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import dev.zt64.tau.resources.*
import dev.zt64.tau.util.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFileAttributes
import java.nio.file.attribute.PosixFilePermission
import java.security.MessageDigest
import kotlin.io.path.*

private enum class Tab(val label: StringResource, val icon: ImageVector) {
    DETAILS(Res.string.details, Icons.Default.Info),
    PERMISSIONS(Res.string.permissions, Icons.Default.Lock),
    CHECKSUMS(Res.string.checksums, Icons.Default.Check)
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
        title = "${path.name} - ${stringResource(Res.string.properties)}",
        icon = icon,
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
                    @Composable
                    fun tab(tab: Tab) {
                        Tab(
                            selected = selectedTab == tab,
                            text = { Text(stringResource(tab.label)) },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = stringResource(tab.label)
                                )
                            },
                            onClick = { selectedTab = tab }
                        )
                    }

                    tab(Tab.DETAILS)
                    tab(Tab.PERMISSIONS)

                    if (path.isRegularFile()) {
                        tab(Tab.CHECKSUMS)
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
                            Tab.CHECKSUMS -> ChecksumsTab(path)
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
            Text(stringResource(Res.string.name))
        },
        trailingContent = {
            Text(path.name)
        }
    )

    ListItem(
        headlineContent = {
            Text(stringResource(Res.string.location))
        },
        trailingContent = {
            Text(path.pathString)
        }
    )

    ListItem(
        headlineContent = {
            Text(stringResource(Res.string.size))
        },
        trailingContent = {
            Text(path.humanReadableSize())
        }
    )

    ListItem(
        headlineContent = {
            Text(stringResource(Res.string.date_created))
        },
        trailingContent = {
            Text(path.creationTime().humanFriendly())
        }
    )

    ListItem(
        headlineContent = {
            Text(stringResource(Res.string.date_modified))
        },
        trailingContent = {
            Text(path.getLastModifiedTime().toInstant().humanFriendly())
        }
    )

    if (!path.isRegularFile()) {
        ListItem(
            headlineContent = {
                Text(stringResource(Res.string.contents))
            },
            trailingContent = {
                val entries = remember {
                    path.listDirectoryEntries()
                }

                Text(pluralStringResource(Res.plurals.items, entries.size, entries.size))
            }
        )
    }
}

@Composable
private fun PermissionsTab(path: Path) {
    val isWindows = remember { System.getProperty("os.name").contains("Windows") }

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Person, stringResource(Res.string.owner))
        },
        headlineContent = {
            Text(stringResource(Res.string.owner))
        },
        trailingContent = {
            Text(path.getOwner()?.name ?: stringResource(Res.string.unknown))
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, stringResource(Res.string.owner_access))
        },
        headlineContent = {
            Text(stringResource(Res.string.owner_access))
        },
        trailingContent = {
            Text(path.getOwner()?.name ?: stringResource(Res.string.unknown))
        }
    )

    HorizontalDivider(thickness = 1.dp)

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Group, stringResource(Res.string.group))
        },
        headlineContent = {
            Text(stringResource(Res.string.group))
        },
        trailingContent = {
            val view = remember { Files.readAttributes(path, PosixFileAttributes::class.java) }

            var expanded by remember { mutableStateOf(false) }
            val options = remember { listOf(view.group().name) }
            val textFieldState = rememberTextFieldState(options[0])

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    value = view.group().name ?: stringResource(Res.string.unknown),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(Res.string.group)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                textFieldState.setTextAndPlaceCursorAtEnd(option)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, stringResource(Res.string.group_access))
        },
        headlineContent = {
            Text(stringResource(Res.string.group_access))
        },
        trailingContent = {
        }
    )

    HorizontalDivider(thickness = 1.dp)

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Public, stringResource(Res.string.global))
        },
        headlineContent = {
            Text(stringResource(Res.string.global))
        }
    )

    ListItem(
        leadingContent = {
            Icon(Icons.Default.Key, stringResource(Res.string.global_access))
        },
        headlineContent = {
            Text(stringResource(Res.string.global_access))
        },
        trailingContent = {
        }
    )

    if (!isWindows) {
        var executable by remember(path) {
            mutableStateOf(path.isExecutable())
        }

        ListItem(
            headlineContent = { Text(stringResource(Res.string.execute)) },
            trailingContent = {
                Switch(
                    checked = executable,
                    onCheckedChange = {
                        try {
                            path.setPosixFilePermissions(
                                path.getPosixFilePermissions().toMutableSet().apply {
                                    if (PosixFilePermission.OWNER_EXECUTE in this) {
                                        remove(PosixFilePermission.OWNER_EXECUTE)
                                    } else {
                                        add(PosixFilePermission.OWNER_EXECUTE)
                                    }
                                }
                            )
                            executable = path.isExecutable()
                        } catch (_: Exception) {
                        }
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun ChecksumsTab(path: Path) {
    val content = remember {
        path.readBytes()
    }

    val md5 = remember(content) {
        MessageDigest.getInstance("MD5").digest(content).toHexString()
    }

    val sha1 = remember(content) {
        MessageDigest.getInstance("SHA-1").digest(content).toHexString()
    }

    val sha256 = remember(content) {
        MessageDigest.getInstance("SHA-256").digest(content).toHexString()
    }

    val sha512 = remember(content) {
        MessageDigest.getInstance("SHA-512").digest(content).toHexString()
    }

    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        @Composable
        fun field(
            value: String,
            label: String
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(value))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(Res.string.copy)
                        )
                    }
                },
                readOnly = true,
                maxLines = 1
            )
        }

        field(md5, "MD5")

        field(sha1, "SHA-1")

        field(sha256, "SHA-256")

        field(sha512, "SHA-512")
    }
}