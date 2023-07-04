package zt.tau

import androidx.compose.foundation.DefaultContextMenuRepresentation
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.path
import dev.icerock.moko.resources.compose.stringResource
import zt.tau.ui.component.ThemeCheckboxItem
import zt.tau.ui.theme.TauTheme
import zt.tau.ui.window.BrowserWindow
import zt.tau.ui.window.colorTheme
import zt.tau.ui.window.selectedFile
import zt.tau.util.copyToClipboard
import java.awt.Dimension

fun main(args: Array<String>) = Tau().main(args)

private class Tau : CliktCommand() {
    val path by argument().path(
        mustExist = true,
        mustBeReadable = true,
        canBeFile = false
    ).optional()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun run() {
        application {
            val windowState = rememberWindowState()

            Window(
                title = "tau",
                icon = painterResource("window-icon.svg"),
                onCloseRequest = ::exitApplication,
                state = windowState
            ) {

                MenuBar {
                    Menu("File", mnemonic = 'F') {
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

                window.minimumSize = Dimension(300, 400)

                TauTheme {
                    CompositionLocalProvider(
                        LocalContextMenuRepresentation provides DefaultContextMenuRepresentation(
                            backgroundColor = MaterialTheme.colorScheme.background,
                            textColor = MaterialTheme.colorScheme.onBackground,
                            itemHoverColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )
                    ) {
                        BrowserWindow()
                    }
                }
            }
        }
    }
}