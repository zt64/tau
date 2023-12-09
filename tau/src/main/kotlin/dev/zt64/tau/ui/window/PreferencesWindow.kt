package dev.zt64.ui.window

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.component.ColorSchemePicker
import org.koin.compose.koinInject

@Composable
fun PreferencesWindow() {
    Surface {
        ColorSchemePicker()
    }
}