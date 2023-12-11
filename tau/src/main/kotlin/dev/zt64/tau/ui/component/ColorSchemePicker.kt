package dev.zt64.tau.ui.component

import androidx.compose.foundation.layout.*
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
import dev.zt64.tau.R
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.model.Theme
import org.koin.compose.koinInject

@Composable
fun ColorSchemePicker(modifier: Modifier = Modifier) {
    val preferencesManager = koinInject<PreferencesManager>()
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = preferencesManager.theme == Theme.DARK,
                onCheckedChange = {
                    preferencesManager.theme = if (it) Theme.DARK else Theme.LIGHT
                }
            )

            Text(
                text = R.strings.DARKTHEME
            )
        }

        HarmonyColorPicker(
            modifier = Modifier.fillMaxSize(),
            harmonyMode = ColorHarmonyMode.NONE,
            color = HsvColor.from(Color(preferencesManager.color))
                .copy(value = 1f), // else every color becomes 0,0,0
            onColorChanged = {
                preferencesManager.color =
                    it.toColor().toArgb()
            },
            showBrightnessBar = false
        )
    }
}