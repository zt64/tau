package dev.zt64.tau.ui.widget

import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.model.Bookmark
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.devices
import dev.zt64.tau.ui.component.FileTransferable
import dev.zt64.tau.ui.component.ScrollableContainer
import dev.zt64.tau.ui.component.sidepanel.SidePanelHeaderItem
import dev.zt64.tau.ui.component.sidepanel.SidePanelLocationItem
import dev.zt64.tau.ui.viewmodel.SidePanelViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.io.path.Path

enum class Section {
    PLACES,
    RECENT,
    DEVICES,
    REMOVABLE_DEVICES
}

@Composable
fun SidePanel(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<SidePanelViewModel>()

    Surface(
        modifier = modifier,
        tonalElevation = 5.dp
    ) {
        val scope = rememberCoroutineScope()
        val lazyListState = rememberLazyListState()

        ScrollableContainer(lazyListState) {
            val bookmarks by viewModel.pinned.collectAsState()

            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                viewModel.sections.forEach { section ->
                    when (section) {
                        Section.PLACES -> {
                            item("bookmarks") {
                                SidePanelHeaderItem(
                                    text = "Bookmarks"
                                )
                            }

                            items(bookmarks) { bookmark ->
                                SidePanelLocationItem(
                                    modifier = Modifier.dragAndDropSource {
                                        DragAndDropTransferData(
                                            transferable = DragAndDropTransferable(FileTransferable(listOf(bookmark.path))),
                                            supportedActions = listOf(
                                                DragAndDropTransferAction.Link,
                                                DragAndDropTransferAction.Copy,
                                                DragAndDropTransferAction.Move
                                            )
                                        )
                                    },
                                    data = bookmark,
                                    onClick = { viewModel.navigateToPath(bookmark.path) }
                                )
                            }
                        }
                        Section.RECENT -> {
                            item("recent") {
                                SidePanelHeaderItem(text = "Recent")
                            }
                        }
                        Section.DEVICES -> {
                            item("devices") {
                                SidePanelHeaderItem(text = stringResource(Res.string.devices))
                            }

                            items(
                                items = viewModel.roots,
                                key = { it }
                            ) {
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
                        Section.REMOVABLE_DEVICES -> {
                        }
                    }
                }
            }
        }
    }
}