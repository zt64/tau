package dev.zt64.tau.ui.component.sidepanel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.tau.R
import dev.zt64.tau.model.Bookmark

@OptIn(ExperimentalFoundationApi::class)
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
                    label = R.strings.OPEN_IN_NEW_TAB,
                    onClick = { }
                ),
                ContextMenuItem(
                    label = R.strings.OPEN_IN_NEW_WINDOW,
                    onClick = { }
                )
            )
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