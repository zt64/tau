package zt.tau.util

import androidx.compose.ui.platform.ClipboardManager
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.Transferable

val ClipboardManager.systemClipboard: Clipboard by lazy { Toolkit.getDefaultToolkit().systemClipboard }

fun ClipboardManager.setContents(content: Transferable, owner: ClipboardOwner?) = systemClipboard.setContents(content, owner)