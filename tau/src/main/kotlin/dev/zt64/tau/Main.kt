package dev.zt64.tau

import androidx.compose.animation.core.tween
import androidx.compose.foundation.DefaultContextMenuRepresentation
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.path
import com.godaddy.android.colorpicker.HsvColor
import com.materialkolor.AnimatedDynamicMaterialTheme
import dev.zt64.tau.di.managerModule
import dev.zt64.tau.di.screenModelModule
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.model.Theme
import dev.zt64.tau.ui.component.MiniTextButton
import dev.zt64.tau.ui.window.BrowserWindow
import dev.zt64.ui.window.PreferencesWindow
import org.jetbrains.skiko.setSystemLookAndFeel
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import java.awt.Dimension
import java.nio.file.Path

fun main(args: Array<String>) = EntryPoint().main(args)

private class EntryPoint : CliktCommand() {
    val path by argument().path(
        mustExist = true,
        mustBeReadable = true,
        canBeFile = false
    ).optional()

    override fun run() = tau(path)
}

fun tau(path: Path?) {
    setSystemLookAndFeel()

    application {
        KoinApplication(
            application = {
                modules(managerModule, screenModelModule)
            }
        ) {
            val windowState = rememberWindowState()

            val preferencesManager = koinInject<PreferencesManager>()

            val updatedHsvColor by remember(preferencesManager.color) {
                mutableStateOf(HsvColor.from(Color(preferencesManager.color)))
            }

            AnimatedDynamicMaterialTheme(
                seedColor = updatedHsvColor.toColor(),
                useDarkTheme = preferencesManager.theme == Theme.DARK,
                animationSpec = tween()
            ) {
                DialogWindow(
                    onCloseRequest = {},
                    state = rememberDialogState(size = DpSize(120.dp, 240.dp)),
                    title = "",
                    resizable = false
                ) {
                    PreferencesWindow()

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
                            tonalElevation = 0.dp,
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
                                        Text(R.Strings.FILE)
                                    }

                                    DropdownMenu(
                                        expanded = showFileDialog,
                                        onDismissRequest = { showFileDialog = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(R.Strings.COPY)
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
                                        Text(R.Strings.EDIT)
                                    }

                                    DropdownMenu(
                                        expanded = showFileDialog,
                                        onDismissRequest = { showFileDialog = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(R.Strings.COPY)
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
                                        Text(R.Strings.VIEW)
                                    }

                                    DropdownMenu(
                                        expanded = showFileDialog,
                                        onDismissRequest = { showFileDialog = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(R.Strings.COPY)
                                            },
                                            onClick = {

                                            }
                                        )
                                    }
                                }
                            }
                        }

                        CompositionLocalProvider(
                            LocalContextMenuRepresentation provides DefaultContextMenuRepresentation(
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                textColor = MaterialTheme.colorScheme.onSurface,
                                itemHoverColor = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        ) {
                            BrowserWindow()
                        }
                    }
                }
            }
        }
    }
}