package dev.zt64.tau.ui.widget.toolbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
    open fun Content(
        enabled: Boolean = true,
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

    @Composable
    open fun ToggleContent(
        enabled: Boolean = true,
        toggled: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        PlainTooltipBox(
            tooltipContent = { Text(stringResource(text)) }
        ) {
            FilledTonalIconToggleButton(
                checked = toggled,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
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
    data object ViewMode : ToolbarEntry()

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
        icon = Icons.Default.Search,
        text = Res.string.search
    )

    data object AddBookmark : ButtonEntry(
        icon = Icons.Default.BookmarkAdd,
        text = Res.string.bookmark_add
    )

    data object CreateNew : ButtonEntry(
        icon = Icons.AutoMirrored.Default.ArrowBack,
        text = Res.string.new_file
    )

    data object Split : ButtonEntry(
        icon = Icons.Default.VerticalSplit,
        text = Res.string.split_view
    )

    data object OpenMenu : ButtonEntry(
        icon = Icons.Default.Menu,
        text = Res.string.back
    )

    data object NewTab : ButtonEntry(
        icon = Icons.Default.Tab,
        text = Res.string.new_tab
    )

    data object NewWindow : ButtonEntry(
        icon = Icons.Default.Tab,
        text = Res.string.new_window
    )
}