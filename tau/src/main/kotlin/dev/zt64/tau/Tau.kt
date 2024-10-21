package dev.zt64.tau

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import com.godaddy.android.colorpicker.HsvColor
import dev.zt64.tau.di.managerModule
import dev.zt64.tau.di.viewModelModule
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.manager.ShortcutsManager
import dev.zt64.tau.model.Theme
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.window_icon
import dev.zt64.tau.ui.component.MenuBar
import dev.zt64.tau.ui.theme.Theme
import dev.zt64.tau.ui.window.BrowserWindow
import dev.zt64.tau.ui.window.preferences.PreferencesWindow
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import java.awt.Dimension

@Composable
fun Tau(onCloseRequest: () -> Unit) {
    KoinApplication(
        application = {
            modules(managerModule, viewModelModule)
        }
    ) {
        val preferencesManager = koinInject<PreferencesManager>()
        val shortcutsManager = koinInject<ShortcutsManager>()

        val updatedHsvColor by remember(preferencesManager.color) {
            mutableStateOf(HsvColor.from(Color(preferencesManager.color)).toColor())
        }

        Theme(
            seedColor = updatedHsvColor,
            isDarkTheme = preferencesManager.theme == Theme.DARK
        ) {
            var showPreferences by rememberSaveable { mutableStateOf(false) }
            var showMenuBar by rememberSaveable { mutableStateOf(true) }

            if (showPreferences) {
                PreferencesWindow(
                    onCloseRequest = { showPreferences = false }
                )
            }

            Window(
                title = "tau",
                icon = painterResource(Res.drawable.window_icon),
                onCloseRequest = onCloseRequest
                // onKeyEvent = {
                //     if (it.type == KeyEventType.KeyDown) {
                //         when (it.key) {
                //             Key.AltLeft -> showMenuBar = !showMenuBar
                //             else -> return@Window false
                //         }
                //
                //         return@Window true
                //     }
                //
                //     false
                // }
            ) {
                LaunchedEffect(Unit) {
                    window.minimumSize = Dimension(300, 400)
                }

                Column {
                    if (showMenuBar) {
                        MenuBar(
                            onClickPreferences = {
                                showPreferences = true
                            }
                        )
                    }

                    BrowserWindow()
                }
            }
        }
    }
}