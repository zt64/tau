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
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun BehaviorPreferences() {
    val preferencesManager = koinInject<PreferencesManager>()
    val scope = rememberCoroutineScope()

    Column {
        val behaviorSettings by preferencesManager.behaviorSettings.collectAsState()
        val viewSettings by preferencesManager.viewSettings.collectAsState()
        val appearanceSettings by preferencesManager.appearanceSettings.collectAsState()

        PreferenceItem(
            headlineContent = { Text(stringResource(Res.string.open_item_action)) },
            trailingContent = {
                var expanded by rememberSaveable { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        value = stringResource(behaviorSettings.openItemAction.s),
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        val scope = rememberCoroutineScope()
                        OpenItemAction.entries.forEach { action ->
                            DropdownMenuItem(
                                text = { Text(stringResource(action.s)) },
                                onClick = {
                                    scope.launch {
                                        preferencesManager.behaviorSettings.update { settings ->
                                            settings.copy(
                                                behavior = settings.behavior.copy(openItemAction = action))
                                        }
                                    }
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
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        value = stringResource(viewSettings.sortType.displayName),
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
                                    scope.launch {
                                        preferencesManager.viewSettings.update { settings ->
                                            settings.copy(view = settings.view.copy(sortType = type))
                                        }
                                    }
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
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        value = stringResource(viewSettings.sortDirection.s),
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
                                    scope.launch {
                                        preferencesManager.viewSettings.update { settings ->
                                            settings.copy(view = settings.view.copy(sortDirection = direction))
                                        }
                                    }
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
                    checked = appearanceSettings.truncateNames,
                    onCheckedChange = {
                        scope.launch {
                            preferencesManager.appearanceSettings.update { settings ->
                                settings.copy(appearance = settings.appearance.copy(truncateNames = it))
                            }
                        }
                    }
                )
            }
        )

        if (appearanceSettings.truncateNames) {
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
                    checked = behaviorSettings.showHiddenFiles,
                    onCheckedChange = {
                        scope.launch {
                            preferencesManager.behaviorSettings.update { settings ->
                                settings.copy(behavior = settings.behavior.copy(showHiddenFiles = it))
                            }
                        }
                    }
                )
            }
        )
    }
}