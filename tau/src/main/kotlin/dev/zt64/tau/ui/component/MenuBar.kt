package dev.zt64.tau.ui.component

import Res
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuBar(
    onClickPreferences: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            MenuBarItem(
                label = { Text(Res.string.file) },
                content = {
                    DropdownMenuItem(
                        text = { Text(Res.string.settings) },
                        onClick = onClickPreferences
                    )
                }
            )

            MenuBarItem(
                label = { Text(Res.string.edit) },
                content = {
                    DropdownMenuItem(
                        text = { Text(Res.string.copy) },
                        onClick = {
                        }
                    )
                }
            )

            MenuBarItem(
                label = { Text(Res.string.view) },
                content = {
                    DropdownMenuItem(
                        text = { Text(Res.string.copy) },
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
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
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