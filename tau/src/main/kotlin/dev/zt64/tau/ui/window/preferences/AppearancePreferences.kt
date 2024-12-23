package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import dev.zt64.tau.model.Theme
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.dark
import dev.zt64.tau.ui.viewmodel.PreferencesViewModel
import dev.zt64.tau.ui.window.ColumnsList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppearancePreferences() {
    val viewModel = koinViewModel<PreferencesViewModel>()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ListItem(
            headlineContent = { Text(stringResource(Res.string.dark)) },
            trailingContent = {
                Switch(
                    checked = viewModel.preferences.theme == Theme.DARK,
                    onCheckedChange = {
                        viewModel.preferences.theme = if (it) Theme.DARK else Theme.LIGHT
                    }
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Accent color")
            Spacer(Modifier.weight(1f))
            HarmonyColorPicker(
                modifier = Modifier.size(128.dp),
                harmonyMode = ColorHarmonyMode.NONE,
                color = HsvColor
                    .from(Color(viewModel.preferences.color))
                    .copy(value = 1f),
                // else every color becomes 0,0,0
                onColorChanged = {
                    viewModel.preferences.color = it.toColor().toArgb()
                },
                showBrightnessBar = false
            )
        }

        ColumnsList()
    }
}