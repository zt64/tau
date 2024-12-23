package dev.zt64.tau.ui.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.cancel
import dev.zt64.tau.resources.rename
import dev.zt64.tau.ui.component.Dialog
import org.jetbrains.compose.resources.stringResource

enum class RenameOption(val label: String) {
    AUTO("Auto"),
    SEARCH_AND_REPLACE("Search & Replace"),
    NUMBERING("Numbering")
}

sealed interface Rename {
    data class Numbering(val start: Int)
}

/**
 * Dialog to handle renaming multiple items
 */
@Composable
fun RenameItemsDialog(
    items: List<String>,
    onDismissRequest: () -> Unit
) {
    Dialog(
        title = {
            Text("Rename items")
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.rename))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel))
            }
        },
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            var selectedOption by remember { mutableStateOf(RenameOption.AUTO) }
            var renamedItems by remember(selectedOption) { mutableStateOf(items) }

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedOption.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    RenameOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option.name)
                            },
                            onClick = {
                                selectedOption = option
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Text(
                text = "Preview",
                style = MaterialTheme.typography.labelLarge
            )

            LazyColumn {
                stickyHeader {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Before")
                        Text("After")
                    }
                }

                items(renamedItems) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item)
                        Text(item)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RenameItemsDialogPreview() {
    RenameItemsDialog(
        items = listOf(
            "guh 1",
            "guh 2",
            "guh 3",
            "guh 4",
            "guh 5",
            "guh 6",
            "guh 7",
            "guh 8",
            "guh 9",
            "guh 6",
            "guh 7",
            "guh 8",
            "guh 9",
            "guh 6",
            "guh 7",
            "guh 8",
            "guh 9"
        ),
        onDismissRequest = {}
    )
}