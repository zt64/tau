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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.io.path.name
import kotlin.math.absoluteValue

@Composable
fun FileHorizontalGrid(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<BrowserViewModel>()

    FileGridContents(modifier) {
        val preferencesManager = koinInject<PreferencesManager>()
        var scale by remember { mutableStateOf(preferencesManager.scale) }
        val gridState = rememberLazyGridState()

        var topLeftOffset by remember { mutableStateOf(Offset(0f, 0f)) }
        var offset by remember { mutableStateOf(Offset(0f, 0f)) }

        LazyHorizontalGrid(
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
            rows = GridCells.FixedSize(scale.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            files(viewModel)
        }

        HorizontalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(minWidth),
            adapter = rememberScrollbarAdapter(gridState)
        )
    }
}

/**
 * File grid layout that displays files in a grid with equal spacing and equal sizing
 *
 * @param modifier
 */
@Composable
fun FileVerticalGrid(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<BrowserViewModel>()

    FileGridContents(modifier) {
        val preferencesManager = koinInject<PreferencesManager>()
        var scale by remember { mutableStateOf(preferencesManager.scale) }
        val gridState = rememberLazyGridState()

        var topLeftOffset by remember { mutableStateOf(Offset.Unspecified) }
        var offset by remember { mutableStateOf(Offset.Unspecified) }

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .onDrag(
                    onDragStart = { topLeftOffset = it },
                    onDrag = {
                        if (offset.isUnspecified) {
                            offset = it
                        } else {
                            offset += it
                        }
                    },
                    onDragEnd = {
                        topLeftOffset = Offset.Unspecified
                        offset = Offset.Unspecified
                    }
                )
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
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = viewModel::clearSelection
                )
                .drawWithCache {
                    this.onDrawWithContent {
                        drawContent()

                        if (!topLeftOffset.isUnspecified && !offset.isUnspecified) {
                            val rectTopLeft = Offset(
                                x = topLeftOffset.x.coerceAtMost(offset.x),
                                y = topLeftOffset.y.coerceAtMost(offset.y)
                            )
                            val rectSize = Size(
                                width = (topLeftOffset.x - offset.x).absoluteValue,
                                height = (topLeftOffset.y - offset.y).absoluteValue
                            )
                            drawRect(
                                color = Color.Red.copy(alpha = 0.7f),
                                topLeft = rectTopLeft,
                                size = rectSize
                            )
                        }
                    }
                },
            state = gridState,
            columns = GridCells.FixedSize(scale.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            files(viewModel)
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(minHeight),
            adapter = rememberScrollbarAdapter(gridState)
        )
    }
}

fun LazyGridScope.files(viewModel: BrowserViewModel) {
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

@Composable
private fun FileGridContents(
    modifier: Modifier = Modifier,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        content()
    }
}