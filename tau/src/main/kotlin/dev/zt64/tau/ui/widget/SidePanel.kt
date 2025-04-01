package dev.zt64.tau.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.model.Bookmark
import dev.zt64.tau.ui.component.ScrollableContainer
import dev.zt64.tau.ui.component.sidepanel.SidePanelHeaderItem
import dev.zt64.tau.ui.component.sidepanel.SidePanelLocationItem
import dev.zt64.tau.ui.viewmodel.SidePanelViewModel
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.io.path.Path

@Composable
fun SidePanel(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<SidePanelViewModel>()

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        tonalElevation = 5.dp
    ) {
        val scope = rememberCoroutineScope()

        val lazyListState = rememberLazyListState()
        val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to -> }

        val bookmarks by viewModel.bookmarks.collectAsState()

        ScrollableContainer(lazyListState) {
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                item("bookmarks") {
                    SidePanelHeaderItem(
                        text = "Bookmarks"
                    )
                }

                items(bookmarks) {
                    SidePanelLocationItem(
                        data = it,
                        onClick = {
                            viewModel.navigateToPath(it.path)
                        }
                    )
                }

                item("disks") {
                    SidePanelHeaderItem(
                        text = "Disks"
                    )
                }

                items(
                    items = viewModel.roots,
                    key = { it }
                ) {
                    ReorderableItem(
                        state = reorderableState,
                        key = it
                    ) { isDragging ->
                        SidePanelLocationItem(
                            data = Bookmark(
                                path = Path(it.mount),
                                name = "${it.label.ifEmpty { it.mount }}",
                                icon = when (it.description.lowercase()) {
                                    "removable drive" -> Icons.Default.Usb
                                    "fixed drive" -> Icons.Default.Storage
                                    "local disk" -> Icons.Default.Storage
                                    // only linux seems to do this one?
                                    "network drive" -> Icons.Default.Lan
                                    "ram disk" -> Icons.Default.Memory
                                    "mount point" -> Icons.Default.Folder
                                    else -> Icons.Default.Storage
                                }
                            ),
                            onClick = {
                                viewModel.navigateToPath(Path(it.mount))
                            }
                        )
                    }
                }
            }
        }
    }
}