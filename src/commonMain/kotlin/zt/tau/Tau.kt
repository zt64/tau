package zt.tau

import androidx.compose.foundation.DefaultContextMenuRepresentation
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.icerock.moko.resources.compose.stringResource
import zt.tau.ui.component.ThemeCheckboxItem
import zt.tau.ui.theme.TauTheme
import zt.tau.ui.window.BrowserWindow
import zt.tau.ui.window.selectedFile
import zt.tau.util.copyToClipboard
import java.awt.Dimension

fun tau() = application {
    val windowState = rememberWindowState()

    Window(
        title = "tau",
        icon = painterResource("window-icon.svg"),
        onCloseRequest = ::exitApplication,
        state = windowState
    ) {
        LaunchedEffect(Unit) {
            window.minimumSize = Dimension(300, 400)
        }

        MenuBar {
            Menu(stringResource(R.strings.file), mnemonic = 'F') {
                Item(
                    text = stringResource(R.strings.copy),
                    onClick = {
                        listOf(selectedFile.toFile()).copyToClipboard()
                    },
                    shortcut = KeyShortcut(Key.C, ctrl = true)
                )

                Item(
                    text = stringResource(R.strings.cut),
                    onClick = {
                    },
                    shortcut = KeyShortcut(Key.X, ctrl = true)
                )

                Item(
                    text = stringResource(R.strings.delete),
                    onClick = {
                    },
                    shortcut = KeyShortcut(Key.Delete)
                )
            }

            Menu("Settings", mnemonic = 'S') {
                Menu("Theme", mnemonic = 'T') {
                    ThemeCheckboxItem("Dark", mnemonic = 'D')
                    ThemeCheckboxItem("Light", mnemonic = 'L')
                }
            }
        }

        TauTheme {
            CompositionLocalProvider(
                LocalContextMenuRepresentation provides DefaultContextMenuRepresentation(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    itemHoverColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ) {
                BrowserWindow()
            }
        }
    }
}
