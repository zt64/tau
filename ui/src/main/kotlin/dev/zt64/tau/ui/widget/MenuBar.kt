package dev.zt64.tau.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.NavigationManager
import dev.zt64.tau.resources.*
import dev.zt64.tau.ui.component.MiniTextButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.system.exitProcess

@Composable
fun MenuBar(
    onClickPreferences: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationManager = koinInject<NavigationManager>()

    Surface(
        modifier = modifier,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val scope = rememberCoroutineScope()
            MenuBarItem(
                label = { Text(stringResource(Res.string.file)) },
                content = {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.new_tab)) },
                        onClick = {
                            scope.launch {
                                navigationManager.newTab()
                            }
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.new_window)) },
                        onClick = {}
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.settings)) },
                        onClick = onClickPreferences
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.close_tab)) },
                        onClick = {
                            scope.launch {
                                navigationManager.closeTab()
                            }
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.quit)) },
                        onClick = { exitProcess(0) }
                    )
                }
            )

            MenuBarItem(
                label = { Text(stringResource(Res.string.edit)) },
                content = {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.copy)) },
                        onClick = {
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.select_all)) },
                        onClick = {
                        }
                    )
                }
            )

            MenuBarItem(
                label = { Text(stringResource(Res.string.view)) },
                content = {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.copy)) },
                        onClick = {
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun MenuBarItem(
    label: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        var showDropdown by rememberSaveable { mutableStateOf(false) }

        MiniTextButton(
            onClick = { showDropdown = true },
            content = label
        )

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            content = content
        )
    }
}