package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.R
import dev.zt64.tau.domain.manager.PreferencesManager
import org.koin.compose.koinInject

@Composable
fun BehaviorPreferences() {
    val preferencesManager = koinInject<PreferencesManager>()
    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        Column () {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = preferencesManager.truncateNames,
                    onCheckedChange = { preferencesManager.truncateNames = it },
                    modifier = Modifier.padding(8.dp),
                )
                Text(
                    text = R.strings.TRUNCATEFILES
                )
            }
            if (preferencesManager.truncateNames) {
                //TODO Numerical input for lines to truncate
            }

        }

    }

}