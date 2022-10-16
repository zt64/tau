package zt.tau.util

import java.io.File
import kotlin.io.path.*

fun File.moveTo(file: File, overwrite: Boolean = false) {
    try {
        copyTo(file, overwrite)
        deleteRecursively()
    } catch (e: FileAlreadyExistsException) {
        // prompt user if they want to overwrite
//         moveTo(this, true)
    }
}

fun File.humanReadableSize(): String {
    val bytes = this.toPath().fileSize().toDouble() // this absolutely shits itself at Longs, so..
    return when {
        bytes >= 1 shl 30 -> "%.1f GB".format(bytes / (1 shl 30))
        bytes >= 1 shl 20 -> "%.1f MB".format(bytes / (1 shl 20))
        bytes >= 1 shl 10 -> "%.0f kB".format(bytes / (1 shl 10))
        else -> "$bytes bytes"
    }
}