package dev.zt64.tau

import Res
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.godaddy.android.colorpicker.HsvColor
import dev.zt64.tau.di.managerModule
import dev.zt64.tau.di.screenModelModule
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.model.Theme
import dev.zt64.tau.ui.component.MiniTextButton
import dev.zt64.tau.ui.theme.Theme
import dev.zt64.tau.ui.window.BrowserWindow
import dev.zt64.tau.ui.window.preferences.ParentWindow
import org.jetbrains.skiko.setSystemLookAndFeel
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import java.awt.Dimension
import java.nio.file.Path

fun tau(path: Path?) {
    setSystemLookAndFeel()

    application {
        KoinApplication(
            application = {
                modules(managerModule, screenModelModule)
            }
        ) {
            val windowState = rememberWindowState()
            val prefsWindowState = rememberWindowState()

            val preferencesManager = koinInject<PreferencesManager>()

            var preferencesWindowVisible by remember { mutableStateOf(false) }

            val updatedHsvColor by remember(preferencesManager.color) {
                mutableStateOf(HsvColor.from(Color(preferencesManager.color)))
            }

            Theme(
                seedColor = updatedHsvColor.toColor(),
                isDarkTheme = preferencesManager.theme == Theme.DARK
            ) {
                if (preferencesWindowVisible) {
                    Window(
                        onCloseRequest = { preferencesWindowVisible = false },
                        state = prefsWindowState,
                        title = Res.string.settings,
                        resizable = true,
                        icon = painterResource("window-icon.svg")
                    ) {
                        ParentWindow()
                    }
                }
                Window(
                    title = "tau",
                    icon = painterResource("window-icon.svg"),
                    onCloseRequest = ::exitApplication,
                    state = windowState
                ) {
                    LaunchedEffect(Unit) {
                        window.minimumSize = Dimension(300, 400)
                    }

                    Column {
                        Surface(
                            tonalElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box {
                                    var showFileDialog by remember { mutableStateOf(false) }

                                    MiniTextButton(
                                        onClick = { showFileDialog = true }
                                    ) {
                                        Text(Res.string.file)
                                    }

                                    DropdownMenu(
                                        expanded = showFileDialog,
                                        onDismissRequest = { showFileDialog = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(Res.string.copy)
                                            },
                                            onClick = {
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                Text(Res.string.settings)
                                            },
                                            onClick = {
                                                preferencesWindowVisible = true
                                                showFileDialog = false
                                            }
                                        )
                                    }
                                }

                                Box {
                                    var showFileDialog by remember { mutableStateOf(false) }

                                    MiniTextButton(
                                        onClick = { showFileDialog = true }
                                    ) {
                                        Text(Res.string.edit)
                                    }

                                    DropdownMenu(
                                        expanded = showFileDialog,
                                        onDismissRequest = { showFileDialog = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(Res.string.copy)
                                            },
                                            onClick = {
                                            }
                                        )
                                    }
                                }

                                Box {
                                    var showFileDialog by remember { mutableStateOf(false) }

                                    MiniTextButton(
                                        onClick = { showFileDialog = true }
                                    ) {
                                        Text(Res.string.view)
                                    }

                                    DropdownMenu(
                                        expanded = showFileDialog,
                                        onDismissRequest = { showFileDialog = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(Res.string.copy)
                                            },
                                            onClick = {
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        BrowserWindow()
                    }
                }
            }
        }
    }
}