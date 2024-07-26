package dev.zt64.tau.ui.component.menu

import Res
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FolderContextMenu(content: @Composable () -> Unit) {
    ContextMenu(
        menuItems = {
            DropdownMenuItem(
                text = {
                    Text(Res.string.open_in_tab)
                },
                onClick = {
                }
            )
            DropdownMenuItem(
                text = {
                    Text(Res.string.open_in_window)
                },
                onClick = {
                }
            )
            DropdownMenuItem(
                text = {
                    Text(Res.string.properties)
                },
                onClick = {
                }
            )
        },
        content = content
    )
}