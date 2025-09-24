package dev.zt64.tau.util

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.Transferable

val Clipboard.systemClipboard: Clipboard by lazy {
    Toolkit.getDefaultToolkit().systemClipboard
}

fun Clipboard.setContents(
    content: Transferable,
    owner: ClipboardOwner?
) = systemClipboard.setContents(content, owner)