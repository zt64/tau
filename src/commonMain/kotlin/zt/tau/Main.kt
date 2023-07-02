package zt.tau

import androidx.compose.foundation.DefaultContextMenuRepresentation
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.path
import com.russhwolf.settings.Settings
import zt.tau.ui.component.ThemeCheckboxItem
import zt.tau.ui.theme.TauTheme
import zt.tau.ui.window.BrowserWindow
import zt.tau.ui.window.colorTheme
import java.awt.Dimension

fun main(args: Array<String>) = Tau().main(args)

private class Tau : CliktCommand() {
    val path by argument().path(
        mustExist = true,
        mustBeReadable = true,
        canBeFile = false
    ).optional()

    override fun run() {
        application {
            val windowState = rememberWindowState()
            val settings: Settings = Settings()

            Window(
                title = "tau",
                icon = painterResource("window-icon.svg"),
                onCloseRequest = ::exitApplication,
                state = windowState
            ) {
                MenuBar {
                    Menu("Settings", mnemonic = 'S') {
                        Menu("Theme", mnemonic = 'T') {
                            ThemeCheckboxItem("Dark")
                            ThemeCheckboxItem("Light")
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