package dev.zt64.tau.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.zt64.tau.ui.component.Thumbnail
import dev.zt64.tau.util.humanReadableSize
import java.nio.file.Path
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.name

enum class FileConflictResolution {
    SKIP,
    REPLACE,
    RENAME
}

@Composable
fun FileConflictDialog(
    file: Path,
    otherFile: Path,
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
                    style = MaterialTheme.typography.headlineMedium
                )

                Column {
                    Text(
                        text = "Source",
                        style = MaterialTheme.typography.titleSmall
                    )
                    File(file)

                    Text(
                        text = "Destination",
                        style = MaterialTheme.typography.titleSmall
                    )
                    File(otherFile)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showApplyToAll) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Apply to all")

                            Checkbox(
                                checked = false,
                                onCheckedChange = { }
                            )
                        }
                    }

                    Button(
                        onClick = { onResolveConflict(FileConflictResolution.SKIP) }
                    ) {
                        Text("Skip")
                    }

                    Button(
                        onClick = { onResolveConflict(FileConflictResolution.REPLACE) }
                    ) {
                        Text("Overwrite")
                    }

                    Button(
                        onClick = { onResolveConflict(FileConflictResolution.RENAME) }
                    ) {
                        Text("Rename")
                    }
                }
            }
        }
    }
}

@Composable
private fun File(file: Path) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Thumbnail(file)

        Column {
            Text(
                text = "Size: ${file.humanReadableSize()}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = file.getLastModifiedTime().toInstant().toString(),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}