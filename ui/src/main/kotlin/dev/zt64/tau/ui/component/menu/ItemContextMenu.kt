package dev.zt64.tau.ui.component.menu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import dev.zt64.tau.domain.manager.NavigationManager
import dev.zt64.tau.resources.*
import dev.zt64.tau.ui.dialog.OpenWithDialog
import dev.zt64.tau.ui.window.PropertiesWindow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import java.awt.datatransfer.StringSelection
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isDirectory

// TODO: Customization?
@Composable
fun ItemContextMenu(
    path: Path,
    content: @Composable () -> Unit
) {
    val navigationManager = koinInject<NavigationManager>()
    val scope = rememberCoroutineScope()
    var showPropertiesWindow by rememberSaveable { mutableStateOf(false) }
    var showOpenWidthDialog by rememberSaveable { mutableStateOf(false) }
    val clipboard = LocalClipboard.current

    if (showPropertiesWindow) {
        PropertiesWindow(path = path, onCloseRequest = { showPropertiesWindow = false })
    }

    if (showOpenWidthDialog) {
        OpenWithDialog(
            path = path,
            onDismissRequest = { showOpenWidthDialog = false },
        )
    }

    ContextMenu(
        menuItems = {
            if (path.isDirectory()) {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(Res.string.open_in_tab))
                    },
                    onClick = {
                        scope.launch {
                            navigationManager.newTab(path)
                        }
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(stringResource(Res.string.open_in_window))
                    },
                    onClick = {
                    }
                )
            } else {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(Res.string.open_with))
                    },
                    onClick = {
                        showOpenWidthDialog = true
                    }
                )
            }
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.copy))
                },
                onClick = {
                }
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.copy_location))
                },
                onClick = {
                    scope.launch {
                        clipboard.setClipEntry(ClipEntry(StringSelection(path.absolutePathString())))
                    }
                }
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.cut))
                },
                onClick = {
                }
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.paste))
                },
                onClick = {
                    scope.launch {

                    }
                }
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.delete))
                },
                onClick = {
                    path.deleteIfExists()
                }
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.properties))
                },
                onClick = {
                    showPropertiesWindow = true
                }
            )
        },
        content = content
    )

}