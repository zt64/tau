package dev.zt64.tau.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.zt64.tau.ui.component.Dialog
import java.nio.file.Path
import kotlin.io.path.Path

@Composable
fun OpenWithDialog(
    path: Path,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        title = {
            Text("Choose an application to open ${path.fileName}")
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Ok")
            }
        },
        onDismissRequest = onDismissRequest,
    ) {
        Column {
            LazyColumn {
                item {

                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewOpenWithDialog() {
    OpenWithDialog(
        path = Path("/a/b/c"),
        onDismissRequest = {},
    )
}