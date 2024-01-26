package dev.zt64.tau.util

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.time.Instant

fun File.moveTo(
    file: File,
    overwrite: Boolean = false
) {
    try {
        copyTo(file, overwrite)
        deleteRecursively()
    } catch (e: FileAlreadyExistsException) {
        // prompt user if they want to overwrite
//         moveTo(this, true)
    }
}

fun File.humanReadableSize(): String {
    val bytes = length().toDouble() // this absolutely shits itself at Longs, so..
    return when {
        bytes >= 1 shl 30 -> "%.1f GB".format(bytes / (1 shl 30))
        bytes >= 1 shl 20 -> "%.1f MB".format(bytes / (1 shl 20))
        bytes >= 1 shl 10 -> "%.0f kB".format(bytes / (1 shl 10))
        else -> "${bytes.toInt()} bytes"
    }
}

fun Path.creationTime(): Instant =
    Files.readAttributes(this, BasicFileAttributes::class.java).creationTime().toInstant()