package dev.zt64.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.ui.component.ColorSchemePicker

@Composable
fun PreferencesWindow() {
    Surface {
        Column () {
            ColorSchemePicker(modifier = Modifier.fillMaxSize())
        }
    }

}