package zt.tau.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import java.nio.file.Path
import kotlin.io.path.absolutePathString

@Composable
fun PathBar(currentLocation: Path) {
    var editing by remember { mutableStateOf(false) }
    val segments = currentLocation
        .absolutePathString()
        .split("/")
        .filter(String::isNotBlank)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            FilledTonalButton(
                shape = RoundedCornerShape(16.dp),
                onClick = {

                }
            ) {
                Text("/")
            }
        }

        itemsIndexed(segments) { index, segment ->
            FilledTonalButton(
                shape = RoundedCornerShape(16.dp),
                onClick = {

                }
            ) {
                Text(segment)
            }
        }
    }

    //    TextField(
    //        value = "$editingLocation",
    //        singleLine = true,
    //        onValueChange = {
    //            editingLocation = Path(it)
    //        }
    //    )

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