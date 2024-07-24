package dev.zt64.tau.ui.component.tooltip

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlainTooltipBox(
    state: TooltipState = rememberTooltipState(),
    tooltipContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        state = state,
        tooltip = {
            PlainTooltip(content = tooltipContent)
        },
        content = content
    )
}