package dev.zt64.tau.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.io.path.name

@Composable
fun FileGrid(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<BrowserViewModel>()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        val preferencesManager = koinInject<PreferencesManager>()
        var scale by remember { mutableStateOf(preferencesManager.scale) }
        val gridState = rememberLazyGridState()

        var topLeftOffset by remember { mutableStateOf(Offset(0f, 0f)) }
        var offset by remember { mutableStateOf(Offset(0f, 0f)) }

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .onPointerEvent(
                    eventType = PointerEventType.Scroll,
                    pass = PointerEventPass.Initial
                ) { ev ->
                    if (!ev.keyboardModifiers.isCtrlPressed) return@onPointerEvent

                    scale += ev
                        .changes
                        .first()
                        .scrollDelta
                        .y
                        .toInt()
                    scale = scale.coerceAtLeast(78)
                    preferencesManager.scale = scale
                }.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = viewModel::clearSelection
                ).drawWithCache {
                    this.onDrawWithContent {
                        drawContent()
                        // drawRect(
                        //     color = Color.Red.copy(alpha = 0.3f),
                        //     size = size,
                        //     topLeft = topLeftOffset
                        // )
                    }
                }.onDrag(
                    onDragStart = { topLeftOffset = it },
                    onDrag = { offset = it },
                    onDragEnd = {
                        topLeftOffset = Offset(0f, 0f)
                        offset = Offset(0f, 0f)
                    }
                ),
            state = gridState,
            columns = GridCells.FixedSize(scale.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            items(
                items = viewModel.contents,
                key = { it.name }
            ) { path ->
                val selected by remember(viewModel.selected) {
                    derivedStateOf { path in viewModel.selected }
                }

                FileItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (selected) {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                    selected = selected,
                    path = path,
                    onSelect = { viewModel.selectItems(path) },
                    onOpen = { viewModel.open(path) }
                )
            }
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(minHeight),
            adapter = rememberScrollbarAdapter(gridState)
        )
    }

    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItem(
                    label = "h"
                ) {
                }
            )
        }
    ) {
    }
}