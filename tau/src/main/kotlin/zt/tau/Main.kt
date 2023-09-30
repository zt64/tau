package zt.tau

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.path

fun main(args: Array<String>) = EntryPoint().main(args)

private class EntryPoint : CliktCommand() {
    val path by argument().path(
        mustExist = true,
        mustBeReadable = true,
        canBeFile = false
    ).optional()

    override fun run() = tau(path)
}