package dev.zt64.tau.ui.widget.browse

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.model.DetailColumnType
import dev.zt64.tau.domain.model.Direction
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.items
import dev.zt64.tau.ui.component.ScrollableContainer
import dev.zt64.tau.ui.component.Thumbnail
import dev.zt64.tau.ui.component.menu.ItemContextMenu
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import dev.zt64.tau.util.dirSize
import dev.zt64.tau.util.humanReadableSize
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import java.nio.file.Files
import kotlin.io.path.isDirectory
import kotlin.io.path.name

@Composable
fun DetailList(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<BrowserViewModel>()

    val columns = remember {
        listOf(
            DetailColumnType.NAME,
            DetailColumnType.TYPE,
            DetailColumnType.SIZE
        )
    }
    val scope = rememberCoroutineScope()
    val verticalLazyListState = rememberLazyListState()

    ScrollableContainer(verticalLazyListState) {
        Table(
            modifier = modifier.fillMaxSize(),
            verticalLazyListState = verticalLazyListState,
            columnCount = columns.size,
            rowCount = viewModel.contents.size,
            row = { rowIndex, content ->
                val path = viewModel.contents[rowIndex]

                ItemContextMenu(path) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isHovered by interactionSource.collectIsHoveredAsState()
                    val selected by remember(viewModel.selected) {
                        derivedStateOf { path in viewModel.selected }
                    }

                    Surface(
                        modifier = Modifier
                            .hoverable(interactionSource)
                            .selectable(
                                selected = selected,
                                onClick = { viewModel.selectItems(path) },
                                interactionSource = interactionSource,
                                indication = LocalIndication.current
                            )
                            .combinedClickable(
                                onClick = {},
                                onDoubleClick = {
                                    scope.launch {
                                        viewModel.open(path)
                                    }
                                }
                            ),
                        color = if (rowIndex % 2 == 0) {
                            MaterialTheme.colorScheme.background
                        } else {
                            MaterialTheme.colorScheme.surfaceColorAtElevation(0.5.dp)
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            content()
                        }
                    }
                }
            },
            headerRow = { content ->
                Surface {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        content()
                    }
                }
            },
            cellContent = { columnIndex, rowIndex ->
                val item = viewModel.contents[rowIndex]
                val column = columns[columnIndex]

                when (column) {
                    DetailColumnType.NAME -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Thumbnail(
                                modifier = Modifier.size(28.dp),
                                path = item
                            )
                            // Icon(
                            //     modifier = Modifier.size(28.dp),
                            //     imageVector = if (item.isDirectory()) {
                            //         Icons.Default.Folder
                            //     } else {
                            //         Icons.Default.FilePresent
                            //     },
                            //     contentDescription = null
                            // )

                            Spacer(Modifier.width(4.dp))

                            Text(
                                text = item.name
                            )
                        }
                    }
                    DetailColumnType.TYPE -> {
                        Text(
                            text = if (item.isDirectory()) {
                                "Directory"
                            } else {
                                try {
                                    Files.probeContentType(item)!!
                                } catch (_: Exception) {
                                    "Unknown"
                                }
                            },
                            maxLines = 1
                        )
                    }
                    DetailColumnType.SIZE -> {
                        Text(
                            text = if (item.isDirectory()) {
                                val itemCount = remember { item.dirSize() }

                                pluralStringResource(Res.plurals.items, itemCount ?: 0, itemCount ?: "?")
                            } else {
                                item.humanReadableSize()
                            },
                            maxLines = 1
                        )
                    }
                    else -> {}
                }
            },
            headerContent = { columnIndex ->
                val column = columns[columnIndex]

                Row(
                    modifier = Modifier.clickable { viewModel.sortBy(column) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(column.displayName),
                        maxLines = 1
                    )

                    if (viewModel.sortType == column) {
                        Icon(
                            modifier = Modifier.size(22.dp),
                            imageVector = if (viewModel.sortDirection == Direction.ASCENDING) {
                                Icons.Default.ArrowUpward
                            } else {
                                Icons.Default.ArrowDownward
                            },
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun Table(
    columnCount: Int,
    rowCount: Int,
    modifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    row: @Composable (rowIndex: Int, content: @Composable RowScope.() -> Unit) -> Unit,
    headerRow: @Composable (content: @Composable RowScope.() -> Unit) -> Unit,
    cellContent: @Composable RowScope.(columnIndex: Int, rowIndex: Int) -> Unit,
    headerContent: @Composable BoxScope.(columnIndex: Int) -> Unit
) {
    Box(
        modifier = modifier.then(Modifier.horizontalScroll(horizontalScrollState))
    ) {
        val columnWidths = remember { mutableStateMapOf<Int, Int>() }

        LazyColumn(
            modifier = Modifier.matchParentSize(),
            state = verticalLazyListState
        ) {
            stickyHeader {
                headerRow {
                    (0 until columnCount).forEach { columnIndex ->
                        Box(
                            modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)

                                val existingWidth = columnWidths[columnIndex] ?: 0
                                val maxWidth = maxOf(existingWidth, placeable.width)

                                if (maxWidth > existingWidth) {
                                    columnWidths[columnIndex] = maxWidth
                                }

                                layout(width = maxWidth, height = placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }
                        ) {
                            headerContent(columnIndex)
                        }
                    }
                }
            }
            items(rowCount) { rowIndex ->
                row(rowIndex) {
                    (0 until columnCount).forEach { columnIndex ->
                        Box(
                            modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)

                                val existingWidth = columnWidths[columnIndex] ?: 0
                                val maxWidth = maxOf(existingWidth, placeable.width)

                                if (maxWidth > existingWidth) {
                                    columnWidths[columnIndex] = maxWidth
                                }

                                layout(width = maxWidth, height = placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }
                        ) {
                            with(this@row) {
                                if (columnIndex == 0) {
                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        this@row.cellContent(columnIndex, rowIndex)
                                    }
                                } else {
                                    this@row.cellContent(columnIndex, rowIndex)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}