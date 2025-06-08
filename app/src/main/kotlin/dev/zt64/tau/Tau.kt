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

@Composable
fun Tau(onCloseRequest: () -> Unit) {
    KoinApplication(
        application = {
            modules(managerModule, viewModelModule)
        }
    ) {
        val preferencesManager = koinInject<PreferencesManager>()
        val shortcutsManager = koinInject<ShortcutsManager>()

        Theme(
            seedColor = { Color(preferencesManager.color) },
            isDarkTheme = preferencesManager.theme == Theme.DARK || preferencesManager.theme == Theme.SYSTEM && isSystemInDarkTheme()
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