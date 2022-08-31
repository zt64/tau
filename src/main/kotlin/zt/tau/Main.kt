package zt.tau

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.path
import zt.tau.ui.theme.TauTheme
import zt.tau.ui.window.BrowserWindow
import java.awt.Dimension

fun main(args: Array<String>) = Tau().main(args)

private class Tau : CliktCommand() {
    val path by argument().path(mustExist = true, canBeFile = false).optional()

    override fun run() = application {
        TauTheme {
            Window(
                title = "tau",
                icon = painterResource("window-icon.svg"),
                onCloseRequest = ::exitApplication
            ) {
                window.minimumSize = Dimension(300, 400)

                BrowserWindow()
            }
        }
    }
}