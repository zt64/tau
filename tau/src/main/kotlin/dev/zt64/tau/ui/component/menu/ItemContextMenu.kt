package dev.zt64.tau.ui.component.menu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import dev.zt64.tau.domain.manager.NavigationManager
import dev.zt64.tau.resources.*
import dev.zt64.tau.ui.window.PropertiesWindow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import java.nio.file.Path

@Composable
fun ItemContextMenu(
    path: Path,
    content: @Composable () -> Unit
) {
    val navigationManager = koinInject<NavigationManager>()
    val scope = rememberCoroutineScope()
    var showPropertiesWindow by rememberSaveable { mutableStateOf(false) }

    if (showPropertiesWindow) {
        PropertiesWindow(path = path, onCloseRequest = { showPropertiesWindow = false })
    }

    ContextMenu(
        menuItems = {
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