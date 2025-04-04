package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.model.DetailColumnType
import dev.zt64.tau.domain.model.Direction
import dev.zt64.tau.domain.model.OpenItemAction
import dev.zt64.tau.resources.*
import dev.zt64.tau.ui.component.preferences.PreferenceItem
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun BehaviorPreferences() {
    val preferencesManager = koinInject<PreferencesManager>()

    Column {
        PreferenceItem(
            headlineContent = { Text(stringResource(Res.string.open_item_action)) },
            trailingContent = {
                var expanded by rememberSaveable { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        value = stringResource(preferencesManager.openItemAction.s),
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
                                text = { Text(stringResource(action.s)) },
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

        PreferenceItem(
            headlineContent = { Text(stringResource(Res.string.sort_type)) },
            trailingContent = {
                var expanded by rememberSaveable { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        value = stringResource(preferencesManager.sortType.displayName),
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
                        DetailColumnType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(stringResource(type.displayName)) },
                                onClick = {
                                    preferencesManager.sortType = type
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
        )

        PreferenceItem(
            headlineContent = { Text(stringResource(Res.string.sort_direction)) },
            trailingContent = {
                var expanded by rememberSaveable { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        value = stringResource(preferencesManager.sortDirection.s),
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
                        Direction.entries.forEach { direction ->
                            DropdownMenuItem(
                                text = { Text(stringResource(direction.s)) },
                                onClick = {
                                    preferencesManager.sortDirection = direction
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
        )

        PreferenceItem(
            headlineContent = {
                Text(stringResource(Res.string.truncate_filename))
            },
            trailingContent = {
                Switch(
                    checked = preferencesManager.truncateNames,
                    onCheckedChange = { preferencesManager.truncateNames = it }
                )
            }
        )

        if (preferencesManager.truncateNames) {
            // TODO Numerical input for lines to truncate
        }

        PreferenceItem(
            headlineContent = {
                Text(stringResource(Res.string.show_hidden_files))
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