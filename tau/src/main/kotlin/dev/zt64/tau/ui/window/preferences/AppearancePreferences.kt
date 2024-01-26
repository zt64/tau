package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.component.ColorSchemePicker
import org.koin.compose.koinInject

@Composable
fun AppearancePreferences() {
    val preferencesManager = koinInject<PreferencesManager>()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row { ColorSchemePicker(modifier = Modifier.fillMaxSize(0.5f)) }
        }
    }
}