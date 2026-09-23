package dev.zt64.tau.ui.widget.browse

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.component.FileItem
import dev.zt64.tau.ui.component.ScrollableContainer
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.io.path.name

@Composable
fun FileHorizontalGrid(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<BrowserViewModel>()
    val preferencesManager = koinInject<PreferencesManager>()
    val appearanceSettings by preferencesManager.appearanceSettings.collectAsState()
    var scale by remember { mutableStateOf(appearanceSettings.scale) }

    FileGrid(
        modifier = modifier
    ) {
        val gridState = rememberLazyGridState()
        val scope = rememberCoroutineScope()

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
                    scope.launch {
                        preferencesManager.appearanceSettings.update { settings ->
                            settings.copy(appearance = settings.appearance.copy(scale = scale))
                        }
                    }
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
    val preferencesManager = koinInject<PreferencesManager>()
    val appearanceSettings by preferencesManager.appearanceSettings.collectAsState()
    var scale by remember { mutableStateOf(appearanceSettings.scale) }

    FileGrid(
        modifier = modifier
    ) {
        val gridState = rememberLazyGridState()
        val scope = rememberCoroutineScope()

        ScrollableContainer(
            state = gridState
        ) {
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
                        scope.launch {
                            preferencesManager.appearanceSettings.update { settings ->
                                settings.copy(appearance = settings.appearance.copy(scale = scale))
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
        }
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
        val scope = rememberCoroutineScope()

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
            onSelect = {
                viewModel.selectItems(path)
            },
            onOpen = {
                scope.launch {
                    viewModel.open(path)
                }
            }
        )
    }
}

@Composable
private fun FileGrid(
    modifier: Modifier = Modifier,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
    ) {
        content()
    }
}