package dev.zt64.tau.ui.component.menu

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

// Use this instead of the ContextMenuArea and ContextMenuItem. They are bad.
@Composable
fun ContextMenu(
    menuItems: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
    properties: PopupProperties = PopupProperties(focusable = true),
    shape: Shape = MenuDefaults.shape,
    containerColor: Color = MenuDefaults.containerColor,
    tonalElevation: Dp = MenuDefaults.TonalElevation,
    shadowElevation: Dp = MenuDefaults.ShadowElevation,
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .contextMenuOpenDetector { expanded = true }
            .then(modifier)
    ) {
        content()

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = offset,
            scrollState = scrollState,
            properties = properties,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            shadowElevation = shadowElevation,
            border = border,
            content = menuItems
        )
    }
}