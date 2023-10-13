package zt.tau.ui.window

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
import org.koin.compose.koinInject
import zt.tau.R
import zt.tau.domain.manager.PreferencesManager
import zt.tau.model.Theme

@Composable
fun PreferencesWindow() {

    val preferencesManager = koinInject<PreferencesManager>()

    Surface {
        Column(
            modifier = Modifier.fillMaxSize()
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
                    modifier = Modifier.clickable {
                        preferencesManager.clear()
                    },
                    text = R.strings.DARK
                )
            }

            HarmonyColorPicker(
                modifier = Modifier.weight(1f, true),
                harmonyMode = ColorHarmonyMode.NONE,
                color = HsvColor.from(Color(preferencesManager.color)).copy(value = 1f), // else every color becomes 0,0,0
                onColorChanged = {
                    preferencesManager.color = it.toColor().toArgb() // TODO: This doesn't make the main window recompose!
                },
                showBrightnessBar = false
            )
        }
    }
}