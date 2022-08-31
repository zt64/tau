package zt.tau.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window

@Composable
fun ConfigWindow(
    onCloseRequest: () -> Unit
) {
    Window(
        onCloseRequest = onCloseRequest
    ) {

    }
}