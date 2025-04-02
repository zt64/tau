package dev.zt64.tau.ui.widget.browse

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.absoluteValue

@Composable
fun BrowseView(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var startOffset by remember { mutableStateOf(Offset.Unspecified) }
    var endOffset by remember { mutableStateOf(Offset.Unspecified) }
    val tertiary = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        startOffset = offset
                        endOffset = offset
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (startOffset.isSpecified) {
                            endOffset = change.position.let { (x, y) ->
                                Offset(
                                    x = x.coerceIn(0f, size.width.toFloat()),
                                    y = y.coerceIn(0f, size.height.toFloat())
                                )
                            }
                        }
                    },
                    onDragEnd = {
                        startOffset = Offset.Unspecified
                        endOffset = Offset.Unspecified
                    },
                    onDragCancel = {
                        startOffset = Offset.Unspecified
                        endOffset = Offset.Unspecified
                    }
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    if (startOffset.isSpecified && endOffset.isSpecified) {
                        val rectTopLeft = Offset(
                            x = minOf(startOffset.x, endOffset.x),
                            y = minOf(startOffset.y, endOffset.y)
                        )
                        val rectSize = Size(
                            width = (startOffset.x - endOffset.x).absoluteValue,
                            height = (startOffset.y - endOffset.y).absoluteValue
                        )

                        drawRect(
                            color = tertiary,
                            topLeft = rectTopLeft,
                            size = rectSize
                        )
                    }
                }
            }
    ) {
        // FIXME: For some reason weight modifier doesn't work with ContextMenuArea
        ContextMenuArea(
            items = {
                buildList {
                    add(ContextMenuItem("Create New", {}))
                }
            }
        ) {
            content()
        }
    }
}