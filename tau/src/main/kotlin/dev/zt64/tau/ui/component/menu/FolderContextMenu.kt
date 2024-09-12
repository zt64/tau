package dev.zt64.tau.ui.component.menu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.zt64.tau.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun FolderContextMenu(content: @Composable () -> Unit) {
    ContextMenu(
        menuItems = {
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.open_in_tab))
                },
                onClick = {
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
                }
            )
        },
        content = content
    )
}