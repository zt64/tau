package dev.zt64.tau.ui.widget.toolbar

import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.tau.ui.component.menu.ItemContextMenu
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.name

@Stable
class PathBarState(val location: Path) {
    var currentSegments by mutableStateOf(emptyList<Path>())
    var currentSegmentIndex by mutableIntStateOf(0)

    fun navigateTo(path: Path) {
        currentSegments = currentSegments + path
    }

    fun navigateUp() {

    }

    fun navigateToSegment(index: Int) {}
}

/**
 * Path bar component.
 *
 * @param location
 * @param onClickSegment
 * @param modifier
 */
@Composable
fun PathBar(
    location: Path,
    onClickSegment: (Path) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSegments by remember { mutableStateOf(emptyList<Path>()) }
    var currentSegmentIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    LaunchedEffect(location) {
        if (location !in currentSegments) {
            currentSegments = buildList {
                add(location.root ?: Path.of(""))

                location.fold(this) { acc, part ->
                    acc.apply { add(last().resolve(part)) }
                }
            }

            currentSegmentIndex = currentSegments.lastIndex
        } else {
            currentSegmentIndex = currentSegments.indexOf(location)
        }
    }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(
            items = currentSegments,
            key = { _, path -> path.absolutePathString() }
        ) { index, segment ->
            Segment(
                segment = segment,
                selected = currentSegmentIndex == index,
                leadingIcon = if (index == 0) {
                    {
                        Icon(Icons.Default.Storage, contentDescription = null)
                    }
                } else {
                    null
                },
                onClick = {
                    currentSegmentIndex = index
                    onClickSegment(segment)
                }
            )
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

@Composable
fun Segment(
    segment: Path,
    selected: Boolean,
    leadingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    TooltipArea(
        tooltip = {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 6.dp,
                shadowElevation = 4.dp
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = segment.toString()
                )
            }
        }
    ) {
        ItemContextMenu(segment) {
            FilledTonalButton(
                shape = MaterialTheme.shapes.large,
                onClick = onClick,
                colors = if (selected) {
                    ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                } else {
                    ButtonDefaults.filledTonalButtonColors()
                }
            ) {
                leadingIcon?.let {
                    it()
                    Spacer(Modifier.width(2.dp))
                }

                Text(
                    text = segment.name,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}