package dev.zt64.tau.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.io.path.pathString

@OptIn(KoinExperimentalAPI::class)
@Composable
fun TabsRow() {
    val viewModel = koinViewModel<BrowserViewModel>()

    AnimatedVisibility(
        visible = viewModel.tabs.size > 1,
        enter = expandVertically { it },
        exit = shrinkVertically { it }
    ) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = viewModel.currentTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            edgePadding = 0.dp
        ) {
            viewModel.tabs.forEachIndexed { index, title ->
                val interactionSource = remember { MutableInteractionSource() }

                Tab(
                    selected = viewModel.currentTabIndex == index,
                    onClick = { viewModel.currentTabIndex = index },
                    interactionSource = interactionSource
                ) {
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isHovered by interactionSource.collectIsHoveredAsState()

                        Text(
                            text = title.pathString,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(60.dp)
                        )

                        IconButton(
                            onClick = { viewModel.closeTab(index) },
                            modifier = Modifier.width(20.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}