package zt.tau.util

import zt.tau.ui.component.FileTransferable
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.io.File

val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard

fun List<File>.copyToClipboard() {
    clipboard.setContents(FileTransferable(this), null)
}