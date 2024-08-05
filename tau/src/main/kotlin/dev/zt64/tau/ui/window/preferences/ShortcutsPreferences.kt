package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.zt64.tau.model.KeyModifier
import dev.zt64.tau.model.Shortcut
import dev.zt64.tau.ui.component.preferences.PreferenceItem
import dev.zt64.tau.ui.viewmodel.PreferencesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShortcutsPreferences() {
    val viewModel = koinViewModel<PreferencesViewModel>()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        viewModel.shortcuts.shortcuts.forEach {
            ShortcutItem(
                label = { },
                shortcut = it
            )
        }
    }
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
                when (shortcut) {
                    is Shortcut.Bound -> {
                        val keyText = remember {
                            buildString {
                                if (shortcut.modifiers.isNotEmpty()) {
                                    append(shortcut.modifiers.joinToString(" + ") + " + ")
                                }

                                append(shortcut.key.toString().removePrefix("Key: "))
                            }
                        }

                        Text(keyText)
                    }
                    Shortcut.Unbound -> {
                        Text("...")
                    }
                }
            }

            // if edited show a button to reset to default
            IconButton(
                onClick = {}
            ) {
                Icon(Icons.Default.Restore, contentDescription = "Reset to default")
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
    var detectedKey by remember { mutableStateOf<Key?>(null) }
    var detectedModifiers = mutableStateListOf<KeyModifier>()

    val confirmEnabled by remember(detectedKey, detectedModifiers) {
        mutableStateOf(detectedKey != null && detectedModifiers.isNotEmpty())
    }

    BasicAlertDialog(
        modifier = Modifier.onKeyEvent { ev ->
            println("ev: $ev")
            if (ev.type == KeyEventType.KeyDown) {
                if (
                    ev.key != Key.AltLeft &&
                    ev.key != Key.AltRight &&
                    ev.key != Key.ShiftLeft &&
                    ev.key != Key.ShiftRight &&
                    ev.key != Key.CtrlLeft &&
                    ev.key != Key.CtrlRight &&
                    ev.key != Key.MetaLeft &&
                    ev.key != Key.MetaRight
                ) {
                    detectedKey = ev.key
                }

                when {
                    ev.isAltPressed -> detectedModifiers.add(KeyModifier.Alt)
                    ev.isShiftPressed -> detectedModifiers.add(KeyModifier.Shift)
                    ev.isCtrlPressed -> detectedModifiers.add(KeyModifier.Ctrl)
                    ev.isMetaPressed -> detectedModifiers.add(KeyModifier.Meta)
                }
            }
            true
        }.focusable(),
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = CircleShape
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Column {
                    Text("Press a key combination to set the shortcut.")
                    Text("Detected key: ${detectedKey?.toString() ?: "None"}")
                    Text("Detected modifiers: ${detectedModifiers.joinToString(", ")}")
                }

                Row {
                    FilledTonalButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    FilledTonalButton(
                        enabled = confirmEnabled,
                        onClick = {
                            onConfirm(Shortcut(detectedModifiers, detectedKey!!))
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}