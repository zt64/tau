package dev.zt64.tau

import Res
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.godaddy.android.colorpicker.HsvColor
import dev.zt64.tau.di.managerModule
import dev.zt64.tau.di.viewModelModule
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.model.Theme
import dev.zt64.tau.ui.component.MiniTextButton
import dev.zt64.tau.ui.theme.Theme
import dev.zt64.tau.ui.window.BrowserWindow
import dev.zt64.tau.ui.window.preferences.PreferencesWindow
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

        val updatedHsvColor by remember(preferencesManager.color) {
            mutableStateOf(HsvColor.from(Color(preferencesManager.color)))
        }

        Theme(
            seedColor = updatedHsvColor.toColor(),
            isDarkTheme = preferencesManager.theme == Theme.DARK
        ) {
            var preferencesWindowVisible by remember { mutableStateOf(false) }

            if (preferencesWindowVisible) {
                PreferencesWindow(
                    onCloseRequest = { preferencesWindowVisible = false }
                )
            }

            Window(
                title = "tau",
                icon = painterResource("window-icon.svg"),
                onCloseRequest = onCloseRequest
            ) {
                LaunchedEffect(Unit) {
                    window.minimumSize = Dimension(300, 400)
                }

                val scope = rememberCoroutineScope()

                var showMenuBar by rememberSaveable { mutableStateOf(false) }

                val focusRequester = remember { FocusRequester() }

                Column(
                    modifier = Modifier.onKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.AltLeft) {
                            showMenuBar = !showMenuBar

                            true
                        } else {
                            false
                        }
                    }
                ) {
                    if (showMenuBar) {
                        Surface(
                            modifier = Modifier,
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
                    }

                    BrowserWindow()
                }
            }
        }
    }
}