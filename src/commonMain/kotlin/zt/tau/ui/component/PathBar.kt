package zt.tau.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import zt.tau.ui.window.currentLocation
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

@Composable
fun PathBar(
    location: Path,
    onClickSegment: (Path) -> Unit,
    modifier: Modifier = Modifier
) {
    val rootPath = currentLocation.root

    //var editing by remember { mutableStateOf(false) }
    val segments = remember(location) {
        location
            .absolutePathString()
            .replace("""^[A-Z]:\\""".toRegex(), "")
            .split(File.separatorChar)
            .filter(String::isNotBlank)
    }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            FilledTonalButton(
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    onClickSegment(rootPath)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Storage,
                    null
                )
                Spacer(Modifier.width(2.dp))
                Text(rootPath.absolutePathString())
            }
        }

        itemsIndexed(
            items = segments,
            key = { index, _ -> index }
        ) { _, segment ->
            FilledTonalButton(
                shape = RoundedCornerShape(16.dp),
                onClick = { // this could be cleaner probably
                    onClickSegment(Path(currentLocation.toString().substringBefore(segment) + segment))
                }
            ) {
                Text(
                    text = segment,
                    overflow = TextOverflow.Ellipsis,
                )
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