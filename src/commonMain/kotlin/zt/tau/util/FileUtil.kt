package zt.tau.util

import java.io.File

fun File.moveTo(file: File, overwrite: Boolean = false) {
    try {
        copyTo(file, overwrite)
        deleteRecursively()
    } catch (e: FileAlreadyExistsException) {
        // prompt user if they want to overwrite
//         moveTo(this, true)
    }
}