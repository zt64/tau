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
import androidx.compose.ui.geometry.isSpecified
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

    FileGrid(
        modifier = modifier,
        onClick = viewModel::clearSelection
    ) {
        val preferencesManager = koinInject<PreferencesManager>()
        var scale by remember { mutableStateOf(preferencesManager.scale) }
        val gridState = rememberLazyGridState()

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
                },
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

    FileGrid(
        modifier = modifier,
        onClick = viewModel::clearSelection
    ) {
        val preferencesManager = koinInject<PreferencesManager>()
        var scale by remember { mutableStateOf(preferencesManager.scale) }
        val gridState = rememberLazyGridState()

        LazyVerticalGrid(
            modifier = Modifier
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

private fun LazyGridScope.files(viewModel: BrowserViewModel) {
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
private fun FileGrid(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    var startOffset by remember { mutableStateOf(Offset.Unspecified) }
    var endOffset by remember { mutableStateOf(Offset.Unspecified) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .onDrag(
                onDragStart = { offset ->
                    startOffset = offset
                    endOffset = offset
                },
                onDrag = { dragAmount ->
                    endOffset += dragAmount
                },
                onDragEnd = {
                    startOffset = Offset.Unspecified
                    endOffset = Offset.Unspecified
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    if (startOffset.isSpecified && endOffset.isSpecified) {
                        val rectTopLeft = Offset(
                            x = minOf(startOffset.x, endOffset.x),
                            y = minOf(startOffset.y, endOffset.y)
                        )
                        val rectSize = Size(
                            width = (startOffset.x - endOffset.x).absoluteValue,
                            height = (startOffset.y - endOffset.y).absoluteValue
                        )

                        drawRect(
                            color = Color.Red.copy(alpha = 0.3f),
                            topLeft = rectTopLeft,
                            size = rectSize
                        )
                    }
                }
            }
    ) {
        content()
    }
}