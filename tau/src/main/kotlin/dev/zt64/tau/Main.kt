package dev.zt64.tau

import androidx.compose.ui.window.application
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.path
import org.jetbrains.skiko.setSystemLookAndFeel

fun main(args: Array<String>) = EntryPoint().main(args)

private class EntryPoint : CliktCommand() {
    val path by argument()
        .path(
            mustExist = true,
            mustBeReadable = true,
            canBeFile = false
        ).optional()

    override fun run() {
        setSystemLookAndFeel()

        application {
            Tau(
                onCloseRequest = ::exitApplication
            )
        }
    }
}