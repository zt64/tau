package dev.zt64.tau.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.state.BrowserState
import org.koin.compose.koinInject
import kotlin.io.path.name

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun FileGrid(
    state: BrowserState,
    modifier: Modifier = Modifier
) {
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
        Box(modifier) {
            val preferencesManager = koinInject<PreferencesManager>()
            var scale by remember { mutableStateOf(preferencesManager.scale) }
            val gridState = rememberLazyGridState()

            var topLeftOffset by remember { mutableStateOf(Offset(0f, 0f)) }
            var offset by remember { mutableStateOf(Offset(0f, 0f)) }

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
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
                        onClick = state::clickGrid
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
                        onDragStart = {
                            topLeftOffset = it
                        },
                        onDrag = {
                            offset = it
                        },
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
                    items = state.files,
                    key = { it.name }
                ) { path ->
                    val selected by remember(state.selected) {
                        derivedStateOf { path in state.selected }
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
                        onClick = { state.selectFiles(path) },
                        onDoubleClick = { state.doubleClick(path) }
                    )
                }
            }

            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(gridState)
            )
        }
    }
}