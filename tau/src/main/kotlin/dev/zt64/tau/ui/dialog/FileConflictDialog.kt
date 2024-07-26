package dev.zt64.tau.ui.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.zt64.tau.model.File
import nl.jacobras.humanreadable.HumanReadable
import java.nio.file.Path

enum class FileConflictResolution {
    SKIP,
    REPLACE,
    RENAME
}

@Composable
fun FileConflictDialog(
    file: File,
    otherFile: File,
    onResolveConflict: (FileConflictResolution) -> Unit,
    showApplyToAll: Boolean,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "This folder already contains a file \"${file.name}\"",
                    style = MaterialTheme.typography.titleMedium
                )

                Row {
                    File(file)
                    File(otherFile)
                }

                if (showApplyToAll) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Apply to all"
                        )

                        Checkbox(
                            checked = false,
                            onCheckedChange = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun File(file: File) {
    Column {
        Text(
            text = "Size: ${HumanReadable.fileSize(file.size)}",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "Modified: ${file.lastModified}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun FileConflictDialogPreview() {
    FileConflictDialog(
        file = File(
            path = Path.of(""),
            name = "file",
            isDirectory = false,
            isHidden = false,
            size = 3000,
            lastModified = 0
        ),
        otherFile = File(
            path = Path.of(""),
            name = "file",
            isDirectory = false,
            isHidden = false,
            size = 3000,
            lastModified = 0
        ),
        onResolveConflict = {},
        showApplyToAll = true,
        onDismissRequest = {}
    )
}