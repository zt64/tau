package dev.zt64.tau.ui.widget.toolbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.zt64.tau.resources.*
import dev.zt64.tau.ui.component.tooltip.PlainTooltipBox
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed class ButtonEntry(
    val icon: ImageVector,
    val text: StringResource,
    open val description: StringResource? = null
) : ToolbarEntry(false) {
    @Composable
    open fun Content(onClick: () -> Unit) {
        Content(
            enabled = true,
            onClick = onClick
        )
    }

    @Composable
    open fun Content(
        enabled: Boolean,
        onClick: () -> Unit
    ) {
        PlainTooltipBox(
            tooltipContent = { Text(stringResource(text)) }
        ) {
            FilledTonalIconButton(
                enabled = enabled,
                onClick = onClick
            ) {
                Icon(icon, stringResource(text))
            }
        }
    }
}

sealed class ToolbarEntry(multipleAllowed: Boolean = false) {
    data object Spacer : ToolbarEntry(multipleAllowed = true)
    data object Separator : ToolbarEntry(multipleAllowed = true)
    data object PathBar : ToolbarEntry()

    data object Back : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowBack,
        text = Res.string.back
    )

    data object Forward : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowForward,
        text = Res.string.forward
    )

    data object Up : ButtonEntry(
        icon = Icons.Default.ArrowUpward,
        text = Res.string.up
    )

    data object Refresh : ButtonEntry(
        icon = Icons.Default.Refresh,
        text = Res.string.refresh
    )

    data object Search : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowBack,
        text = Res.string.back
    )

    data object AddBookmark : ButtonEntry(
        icon = Icons.Default.BookmarkAdd,
        text = Res.string.back
    )

    data object CreateNew : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowBack,
        text = Res.string.back
    )

    data object Split : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowBack,
        text = Res.string.back
    )

    data object OpenMenu : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowBack,
        text = Res.string.back
    )

    data object NewTab : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowBack,
        text = Res.string.back
    )

    data object NewWindow : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowBack,
        text = Res.string.back
    )
}