package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import dev.zt64.compose.pipette.CircularColorPicker
import dev.zt64.compose.pipette.HsvColor
import dev.zt64.tau.domain.model.Theme
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.dark
import dev.zt64.tau.ui.viewmodel.PreferencesViewModel
import dev.zt64.tau.ui.window.ColumnsList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppearancePreferences() {
    val viewModel = koinViewModel<PreferencesViewModel>()
    val appearanceSettings by viewModel.preferences.appearanceSettings.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ListItem(
            headlineContent = { Text(stringResource(Res.string.dark)) },
            trailingContent = {
                Switch(
                    checked = appearanceSettings.theme == Theme.DARK,
                    onCheckedChange = { checked ->
                        scope.launch {
                            viewModel.preferences.appearanceSettings.update { settings ->
                                settings.copy(
                                    appearance = settings.appearance.copy(
                                        theme = if (checked) Theme.DARK else Theme.LIGHT
                                    )
                                )
                            }
                        }
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
            CircularColorPicker(
                modifier = Modifier.size(128.dp),
                color = { HsvColor(appearanceSettings.color) },
                onColorChange = { color ->
                    scope.launch {
                        viewModel.preferences.appearanceSettings.update { settings ->
                            settings.copy(
                                appearance = settings.appearance.copy(
                                    color = color.toColor().toArgb().toLong()
                                )
                            )
                        }
                    }
                }
            )
        }

        ColumnsList()
    }
}