package dev.zt64.tau.ui.component.sidepanel

import Res
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.tau.model.Bookmark

@Composable
fun Bookmark(
    data: Bookmark,
    onClick: () -> Unit,
    icon: ImageVector = Icons.Default.Folder
) {
    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItem(
                    label = Res.string.open_in_tab,
                    onClick = { }
                ),
                ContextMenuItem(
                    label = Res.string.open_in_window,
                    onClick = { }
                )
            )
        }
    ) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            state = rememberTooltipState(),
            tooltip = {
                PlainTooltip {
                    Text(text = data.displayName)
                }
            }
        ) {
            ListItem(
                modifier = Modifier
                    .combinedClickable(onClick = onClick)
                    .padding(horizontal = 4.dp)
                    .widthIn(min = 120.dp),
                leadingContent = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                },
                headlineContent = {
                    Text(
                        text = data.displayName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}