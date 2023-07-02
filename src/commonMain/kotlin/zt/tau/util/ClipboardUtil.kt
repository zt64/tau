package zt.tau.util

import androidx.compose.ui.platform.LocalClipboardManager
import zt.tau.ui.component.FileTransferable
import java.awt.Toolkit
import java.io.File

val clipboard = Toolkit.getDefaultToolkit().systemClipboard

fun List<File>.copyToClipboard() {
    clipboard.setContents(FileTransferable(this), null)
}