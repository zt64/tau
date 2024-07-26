package dev.zt64.tau.ui.window.preferences

import Res
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.model.OpenItemAction
import org.koin.compose.koinInject

@Composable
fun BehaviorPreferences() {
    val preferencesManager = koinInject<PreferencesManager>()

    Column {
        ListItem(
            headlineContent = { Text(Res.string.open_item_action) },
            trailingContent = {
                var expanded by rememberSaveable { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        value = preferencesManager.openItemAction.s,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        OpenItemAction.entries.forEach { action ->
                            DropdownMenuItem(
                                text = { Text(action.s) },
                                onClick = {
                                    preferencesManager.openItemAction = action
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
        )

        ListItem(
            headlineContent = {
                Text(Res.string.truncate_filename)
            },
            trailingContent = {
                Switch(
                    checked = preferencesManager.truncateNames,
                    onCheckedChange = { preferencesManager.truncateNames = it },
                    modifier = Modifier.padding(8.dp)
                )
            }
        )

        if (preferencesManager.truncateNames) {
            // TODO Numerical input for lines to truncate
        }

        ListItem(
            headlineContent = {
                Text("Show hidden files")
            },
            supportingContent = {
                Text("Global setting for showing hidden files in the file browser")
            },
            trailingContent = {
                Switch(
                    checked = preferencesManager.showHiddenFiles,
                    onCheckedChange = { preferencesManager.showHiddenFiles = it }
                )
            }
        )
    }
}