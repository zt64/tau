package dev.zt64.ui.window

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.model.Theme
import dev.zt64.tau.ui.component.ColorSchemePicker
import org.koin.compose.koinInject

@Composable
fun PreferencesWindow() {
    val preferencesManager = koinInject<PreferencesManager>()

    Surface {
        ColorSchemePicker()
    }
}