package dev.zt64.tau.ui.component.tooltip

import androidx.compose.material3.TooltipScope
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupPositionProvider

// Composable for a tooltip box. M3 tooltip box for some reason doesn't have a default state ???
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