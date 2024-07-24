package dev.zt64.tau.ui.component.tooltip

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupPositionProvider

// Composable for a tooltip box. M3 tooltip box for some reason doesnt have a default state ???
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipBox(
    positionProvider: PopupPositionProvider,
    tooltip: @Composable TooltipScope.() -> Unit,
    state: TooltipState = rememberTooltipState(),
    modifier: Modifier = Modifier,
    focusable: Boolean = true,
    enableUserInput: Boolean = true,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.TooltipBox(
        positionProvider = positionProvider,
        state = state,
        tooltip = tooltip,
        modifier = modifier,
        focusable = focusable,
        enableUserInput = enableUserInput,
        content = content
    )
}