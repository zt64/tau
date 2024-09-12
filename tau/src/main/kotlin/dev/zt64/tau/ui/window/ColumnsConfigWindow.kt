package dev.zt64.tau.ui.window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.model.DetailColumn
import dev.zt64.tau.model.DetailColumnType
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import sh.calvin.reorderable.ReorderableColumn

@Composable
fun ColumnsConfigWindow(onCloseRequest: () -> Unit) {
    Window(
        onCloseRequest = onCloseRequest
    ) {
    }
}

@Composable
fun ColumnsList() {
    val preferences = koinInject<PreferencesManager>()

    Column {
        Card {
            BoxWithConstraints {
                var columns = remember {
                    DetailColumnType.entries.map {
                        DetailColumn(it, true)
                    }.toMutableList()
                }

                val scrollState = rememberScrollState()

                ReorderableColumn(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(240.dp)
                        .padding(
                            start = 16.dp,
                            end = 24.dp
                        )
                        .verticalScroll(scrollState),
                    list = columns,
                    onSettle = { fromIndex, toIndex ->
                        columns = columns.toMutableList().apply {
                            add(toIndex, removeAt(fromIndex))
                        }
                    }
                ) { index, item, _ ->
                    var enabled by remember { mutableStateOf(true) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .draggableHandle(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = enabled,
                            onCheckedChange = {
                                enabled = it
                            }
                        )

                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(item.type.displayName),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )

                        Spacer(Modifier.width(8.dp))

                        Icon(
                            modifier = Modifier.alpha(0.7f),
                            imageVector = Icons.Default.DragHandle,
                            contentDescription = "Drag handle"
                        )
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .height(minHeight),
                    adapter = rememberScrollbarAdapter(scrollState)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = preferences.autoSizeColumns,
                onCheckedChange = {
                    preferences.autoSizeColumns = it
                }
            )

            Text("Automatically size columns as needed")
        }
    }
}