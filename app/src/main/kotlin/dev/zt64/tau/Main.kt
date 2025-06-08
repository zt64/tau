package dev.zt64.tau

import androidx.compose.ui.window.application
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.transform.theme
import com.github.ajalt.clikt.parameters.types.path
import org.jetbrains.skiko.setSystemLookAndFeel

fun main(args: Array<String>) = EntryPoint().main(args)

private class EntryPoint : CliktCommand() {
    val path by argument()
        .path(
            mustExist = true,
            mustBeReadable = true,
            canBeFile = false
        )
        .optional()
        .help { theme.info("The path to open") }

    override fun run() {
        setSystemLookAndFeel()

        application {
            Tau(onCloseRequest = ::exitApplication)
        }
    }
}