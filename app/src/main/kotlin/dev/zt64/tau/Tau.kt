package dev.zt64.tau

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import dev.zt64.tau.di.managerModule
import dev.zt64.tau.di.viewModelModule
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.manager.ShortcutsManager
import dev.zt64.tau.domain.model.Theme
import dev.zt64.tau.ui.theme.Theme
import dev.zt64.tau.ui.window.BrowserWindow
import dev.zt64.tau.ui.window.preferences.PreferencesWindow
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.dsl.koinConfiguration

@Composable
fun Tau(onCloseRequest: () -> Unit) {
    KoinApplication(
        configuration = koinConfiguration {
            modules(managerModule, viewModelModule)
        }
    ) {
        val preferencesManager = koinInject<PreferencesManager>()
        val appearanceSettings by preferencesManager.appearanceSettings.collectAsState()
        val shortcutsManager = koinInject<ShortcutsManager>()

        Theme(
            seedColor = { Color(appearanceSettings.color) },
            isDarkTheme = (appearanceSettings.theme == Theme.DARK) || ((appearanceSettings.theme == Theme.SYSTEM) && isSystemInDarkTheme())
        ) {
            var showPreferences by rememberSaveable { mutableStateOf(false) }

            if (showPreferences) {
                PreferencesWindow(
                    onCloseRequest = { showPreferences = false }
                )
            }

            BrowserWindow(
                onClickShowPreferences = { showPreferences = true },
                onCloseRequest = onCloseRequest
            )
        }
    }
}