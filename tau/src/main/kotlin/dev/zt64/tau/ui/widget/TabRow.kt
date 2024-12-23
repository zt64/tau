package dev.zt64.tau.ui.widget

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.close_tab
import dev.zt64.tau.ui.component.menu.ItemContextMenu
import dev.zt64.tau.ui.component.tooltip.PlainTooltipBox
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.math.roundToInt

@Composable
fun TabRow() {
    val viewModel = koinViewModel<BrowserViewModel>()

    AnimatedVisibility(
        visible = viewModel.nav.tabs.size > 1,
        enter = expandVertically(tween(5000), expandFrom = Alignment.Top) { it },
        exit = shrinkVertically(tween(5000)) { it }
    ) {
        val currentTabIndex by viewModel.nav.currentTabIndex.collectAsState()

        TabRow(
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(
                    state = rememberScrollableState { delta ->
                        val newIndex = (currentTabIndex + (delta * 0.07f))
                            .roundToInt()
                            .let { index ->
                                when {
                                    index < 0 -> viewModel.nav.tabs.size - 1
                                    index >= viewModel.nav.tabs.size -> 0
                                    else -> index
                                }
                            }
                        viewModel.switchTab(newIndex)

                        delta
                    },
                    orientation = Orientation.Vertical
                ),
            selectedTabIndex = currentTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        ) {
            viewModel.nav.tabs.forEachIndexed { index, path ->
                Tab(
                    path = path,
                    selected = currentTabIndex == index,
                    onClick = { viewModel.switchTab(index) },
                    onClickClose = { viewModel.closeTab(index) }
                )
            }
        }
    }
}

@Composable
private fun Tab(
    path: Path,
    selected: Boolean,
    onClick: () -> Unit,
    onClickClose: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    TabContextMenu(path) {
        PlainTooltipBox(
            tooltipContent = {
                Text(path.absolutePathString())
            }
        ) {
            Tab(
                selected = selected,
                onClick = onClick,
                interactionSource = interactionSource
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .height(26.dp)
                ) {
                    val isHovered by interactionSource.collectIsHoveredAsState()

                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = path.name.ifEmpty { path.pathString },
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )

                    this@Tab.AnimatedVisibility(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        visible = isHovered,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                        ) {
                            IconButton(onClick = onClickClose) {
                                Icon(
                                    modifier = Modifier.size(14.dp),
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(Res.string.close_tab)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabContextMenu(
    path: Path,
    content: @Composable () -> Unit
) {
    ItemContextMenu(path, content)
}