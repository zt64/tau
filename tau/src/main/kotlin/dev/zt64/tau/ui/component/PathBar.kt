package dev.zt64.tau.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.tau.ui.component.menu.FolderContextMenu
import dev.zt64.tau.ui.state.BrowserState
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

/**
 * Path bar component.
 *
 * @param state
 * @param location
 * @param onClickSegment
 * @param modifier
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PathBar(
    state: BrowserState,
    location: Path,
    onClickSegment: (Path) -> Unit,
    modifier: Modifier = Modifier
) {
    val rootPath = location.root

    // var editing by remember { mutableStateOf(false) }
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
                shape = MaterialTheme.shapes.large,
                onClick = { onClickSegment(rootPath) }
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
            val fullPath =
                remember {
                    Path(state.currentLocation.toString().substringBefore(segment) + segment)
                }

            TooltipArea(
                tooltip = {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 6.dp,
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = fullPath.absolutePathString()
                        )
                    }
                }
            ) {
                FolderContextMenu {
                    FilledTonalButton(
                        shape = MaterialTheme.shapes.large,
                        onClick = { onClickSegment(fullPath) }
                    ) {
                        Text(
                            text = segment,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
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