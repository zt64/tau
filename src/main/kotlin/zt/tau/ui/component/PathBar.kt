package zt.tau.ui.component

import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import java.nio.file.Path
import kotlin.io.path.Path

@Composable
fun PathBar(currentLocation: Path) {
    var editing by remember { mutableStateOf(false) }
    var editingLocation by remember { mutableStateOf(currentLocation) }

    TextField(
        value = "$editingLocation",
        singleLine = true,
        onValueChange = {
            editingLocation = Path(it)
        }
    )

    //    if (editing) {
    //        var editingLocation by remember { mutableStateOf(currentLocation.path) }
    //
    //        TextField(
    //            modifier = Modifier.onKeyEvent { keyEvent ->
    //                when (keyEvent.key) {
    //                    Key.Enter, Key.Escape -> {
    //                        editing = false
    //
    //                        true
    //                    }
    //                    else -> false
    //                }
    //            },
    //            value = editingLocation,
    //            onValueChange = {
    //                editingLocation = it
    //            }
    //        )
    //    } else {
    //        Row(
    //            modifier = modifier.clickable {
    //                editing = true
    //            }
    //        ) {
    //            Box(Modifier) {
    //                Text("/")
    //            }
    //            currentLocation.path.split("/").forEach { segment ->
    //                Box(Modifier) {
    //                    Text(segment)
    //                }
    //            }
    //        }
    //    }
}