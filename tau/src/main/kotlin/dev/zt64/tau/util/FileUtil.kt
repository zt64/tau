package dev.zt64.tau.util

import kotlinx.io.IOException
import java.io.File
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.UserDefinedFileAttributeView
import java.time.Instant
import kotlin.io.path.fileSize
import kotlin.io.path.isDirectory

fun File.moveTo(
    file: File,
    overwrite: Boolean = false
) {
    try {
        copyTo(file, overwrite)
        deleteRecursively()
    } catch (e: FileAlreadyExistsException) {
        // prompt user if they want to overwrite
        // moveTo(this, true)
    }
}

fun Path.dirSize() = try {
    Files.newDirectoryStream(this).count()
} catch (_: Exception) {
    null
}

/**
 * Returns the size of the file in a human-readable format, or if it's a directory just the number of items in the directory
 */
fun Path.size(): Long = if (isDirectory()) {
    dirSize()?.toLong() ?: 0
} else {
    fileSize()
}

fun File.humanReadableSize(): String {
    return getHumanReadableSize(length().toDouble())
}

fun Path.humanReadableSize(): String {
    return try {
        getHumanReadableSize(fileSize().toDouble())
    } catch (_: IOException) {
        "0 bytes"
    }
}

private fun getHumanReadableSize(bytes: Double): String {
    return when {
        bytes >= 1 shl 30 -> "%.1f GB".format(bytes / (1 shl 30))
        bytes >= 1 shl 20 -> "%.1f MB".format(bytes / (1 shl 20))
        bytes >= 1 shl 10 -> "%.0f kB".format(bytes / (1 shl 10))
        else -> "${bytes.toInt()} bytes"
    }
}

fun Path.creationTime(): Instant {
    return Files.readAttributes(this, BasicFileAttributes::class.java).creationTime().toInstant()
}

fun Path.setXAttr(
    key: String,
    value: String
) {
    val view = Files.getFileAttributeView(this, UserDefinedFileAttributeView::class.java)
    view.write(key, ByteBuffer.wrap(value.toByteArray(StandardCharsets.UTF_8)))
}

fun Path.getXAttr(key: String): String? {
    val view = Files.getFileAttributeView(this, UserDefinedFileAttributeView::class.java)
    if (view == null || key !in listXAttrs()) return null

    val buffer = ByteBuffer.allocate(view.size(key))
    view.read(key, buffer)
    buffer.flip()
    return StandardCharsets.UTF_8.decode(buffer).toString()
}

fun Path.listXAttrs(): List<String> {
    val view = Files.getFileAttributeView(this, UserDefinedFileAttributeView::class.java)
    return view?.list().orEmpty()
}