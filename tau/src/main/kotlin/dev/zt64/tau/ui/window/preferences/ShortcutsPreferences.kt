package dev.zt64.tau.ui.window.preferences

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.zt64.tau.domain.manager.Shortcut
import dev.zt64.tau.ui.component.preferences.PreferenceItem
import dev.zt64.tau.ui.viewmodel.PreferencesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShortcutsPreferences() {
    val viewModel = koinViewModel<PreferencesViewModel>()
}

@Composable
fun ShortcutItem(
    label: @Composable () -> Unit,
    shortcut: Shortcut,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ShortcutDialog(
            onConfirm = {
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    PreferenceItem(
        headlineContent = label,
        modifier = modifier,
        trailingContent = {
            FilledTonalButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Text(shortcut.toString())
            }
        }
    )
}

/**
 * Dialog for adding a new shortcut.
 *
 */
@Composable
fun ShortcutDialog(
    onConfirm: (Shortcut) -> Unit,
    onDismissRequest: () -> Unit
) {
    var confirmEnabled by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            FilledTonalButton(
                enabled = confirmEnabled,
                onClick = {
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            FilledTonalButton(
                onClick = {
                }
            ) {
                Text("Cancel")
            }
        }
    )
}