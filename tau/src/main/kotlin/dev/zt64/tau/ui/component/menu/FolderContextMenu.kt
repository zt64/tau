package dev.zt64.tau.ui.component.menu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.runtime.Composable

@Composable
fun FolderContextMenu(content: @Composable () -> Unit) {
    ContextMenuArea(
        items = {
            listOf()
        },
        content = content
    )
}