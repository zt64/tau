package dev.zt64.tau.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window

@Composable
fun ColumnsConfigWindow(onCloseRequest: () -> Unit) {
    Window(
        onCloseRequest = onCloseRequest
    ) {
        Column {
        }
    }
}