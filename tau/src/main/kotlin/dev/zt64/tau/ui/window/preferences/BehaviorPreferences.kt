package dev.zt64.tau.ui.window.preferences

import Res
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import org.koin.compose.koinInject

@Composable
fun BehaviorPreferences() {
    val preferencesManager = koinInject<PreferencesManager>()

    Column {
        ListItem(
            headlineContent = {
                Text(Res.string.truncate_filename)
            },
            trailingContent = {
                Switch(
                    checked = preferencesManager.truncateNames,
                    onCheckedChange = { preferencesManager.truncateNames = it },
                    modifier = Modifier.padding(8.dp)
                )
            }
        )

        if (preferencesManager.truncateNames) {
            // TODO Numerical input for lines to truncate
        }

        ListItem(
            headlineContent = {
                Text("Show hidden files")
            },
            supportingContent = {
                Text("Global setting for showing hidden files in the file browser")
            },
            trailingContent = {
                Switch(
                    checked = preferencesManager.showHiddenFiles,
                    onCheckedChange = { preferencesManager.showHiddenFiles = it }
                )
            }
        )
    }
}