package zt.tau.util

import androidx.compose.ui.platform.ClipboardManager
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.Transferable

enum class OperatingSystem {
    WINDOWS, LINUX, MAC, SOLARIS, UNKNOWN
}

val ClipboardManager.systemClipboard: Clipboard by lazy { Toolkit.getDefaultToolkit().systemClipboard }

fun ClipboardManager.setContents(content: Transferable, owner: ClipboardOwner?) = systemClipboard.setContents(content, owner)

fun getOperatingSystem(): OperatingSystem {
    val os = System.getProperty("os.name").lowercase()
    return when {
        os.contains("win") -> {
            OperatingSystem.WINDOWS
        }
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
            OperatingSystem.LINUX
        }
        os.contains("mac") -> {
            OperatingSystem.MAC
        }
        os.contains("sunos") -> {
            OperatingSystem.SOLARIS
        }
        else -> OperatingSystem.UNKNOWN
    }
}
