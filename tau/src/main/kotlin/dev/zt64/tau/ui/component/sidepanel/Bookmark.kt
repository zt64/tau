package dev.zt64.tau.ui.component.sidepanel

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.NavigationManager
import dev.zt64.tau.model.Bookmark
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.io.path.absolutePathString

@Composable
fun Bookmark(
    data: Bookmark,
    onClick: () -> Unit
) {
    val navigationManager: NavigationManager = koinInject()
    val scope = rememberCoroutineScope()

    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItem(
                    label = "Open in new tab",
                    onClick = {
                        scope.launch {
                            navigationManager.newTab(data.path)
                        }
                    }
                ),
                ContextMenuItem(
                    label = "Open in new window",
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
                    Text("${data.displayName} (${data.path.absolutePathString()})")
                }
            }
        ) {
            ListItem(
                modifier = Modifier
                    .combinedClickable(onClick = onClick)
                    .dragAndDropTarget(
                        shouldStartDragAndDrop = {
                            true
                        },
                        target = object : DragAndDropTarget {
                            override fun onDrop(event: DragAndDropEvent): Boolean {
                                return true
                            }
                        }
                    )
                    .padding(horizontal = 4.dp)
                    .widthIn(min = 120.dp),
                leadingContent = {
                    Icon(
                        imageVector = data.icon,
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