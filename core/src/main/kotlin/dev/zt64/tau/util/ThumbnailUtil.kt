package dev.zt64.tau.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.tika.Tika
import org.jetbrains.skia.Image
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readBytes

private val XDG_CACHE_HOME by lazy {
    System.getenv("XDG_CACHE_HOME") ?: (System.getProperty("user.home") + "/.cache")
}

suspend fun Path.getThumbnail(): ImageBitmap? {
    if (hostOs != OS.Linux) return null // only linux support implemented

    return withContext(Dispatchers.IO) {
        val md5Hash = MessageDigest.getInstance("MD5")
            .digest("file://${this@getThumbnail.toAbsolutePath()}".toByteArray())
            .joinToString("") { "%02x".format(it) }
        val thumbPath = Path("$XDG_CACHE_HOME/thumbnails/normal/$md5Hash.png")

        if (thumbPath.exists()) {
            Image.makeFromEncoded(thumbPath.readBytes()).toComposeImageBitmap()
        } else {
            val mimeType = Tika().detect(this.toString())

            if (mimeType.startsWith("image/")) {
                try {
                    Image.makeFromEncoded(this@getThumbnail.readBytes()).toComposeImageBitmap()
                } catch (_: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }
}