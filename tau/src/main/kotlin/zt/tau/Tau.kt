package zt.tau

import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import com.materialkolor.AnimatedDynamicMaterialTheme
import kotlinx.coroutines.flow.debounce
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import zt.tau.di.managerModule
import zt.tau.domain.manager.PreferencesManager
import zt.tau.model.Theme
import zt.tau.ui.window.BrowserWindow
import java.awt.Dimension
import java.nio.file.Path
import javax.swing.UIManager

fun tau(path: Path?) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    application {
        KoinApplication(
            application = {
                modules(managerModule)
            }
        ) {
            val applicationCoroutineScope = rememberCoroutineScope()
            val windowState = rememberWindowState()


            val preferencesManager = koinInject<PreferencesManager>()

            var updatedHsvColor by remember {
                mutableStateOf(HsvColor.from(Color(preferencesManager.color)))
            }

            LaunchedEffect(updatedHsvColor) {
                snapshotFlow { updatedHsvColor }
                    .debounce(1000)
                    .collect {
                        preferencesManager.color = it.toColor().toArgb()
                    }
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
                    Surface {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Max)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    checked = preferencesManager.theme == Theme.DARK,
                                    onCheckedChange = {
                                        preferencesManager.theme = if (it) Theme.DARK else Theme.LIGHT
                                    }
                                )

                                Text(
                                    modifier = Modifier.clickable {
                                        preferencesManager.clear()
                                    },
                                    text = R.strings.DARK
                                )
                            }

                            HarmonyColorPicker(
                                modifier = Modifier.weight(1f, true),
                                harmonyMode = ColorHarmonyMode.NONE,
                                color = updatedHsvColor,
                                onColorChanged = { updatedHsvColor = it },
                                showBrightnessBar = false
                            )
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
                                            Text(R.strings.FILE)
                                        }

                                        DropdownMenu(
                                            expanded = showFileDialog,
                                            onDismissRequest = { showFileDialog = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(R.strings.COPY)
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
                                            Text(R.strings.EDIT)
                                        }

                                        DropdownMenu(
                                            expanded = showFileDialog,
                                            onDismissRequest = { showFileDialog = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(R.strings.COPY)
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
                                            Text(R.strings.VIEW)
                                        }

                                        DropdownMenu(
                                            expanded = showFileDialog,
                                            onDismissRequest = { showFileDialog = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(R.strings.COPY)
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
}

val ButtonDefaults.MiniTextButtonContentPadding: PaddingValues
    get() = PaddingValues(
        horizontal = 8.dp,
        vertical = 4.dp
    )

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("INVISIBLE_MEMBER")
@Composable
fun MiniTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.textShape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.MiniTextButtonContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    val containerColor = colors.containerColor(enabled).value
    val contentColor = colors.contentColor(enabled).value
    val shadowElevation = elevation?.shadowElevation(enabled, interactionSource)?.value ?: 0.dp
    val tonalElevation = elevation?.tonalElevation(enabled, interactionSource)?.value ?: 0.dp
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        Surface(
            onClick = onClick,
            modifier = modifier.semantics { role = Role.Button },
            enabled = enabled,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            shadowElevation = shadowElevation,
            border = border,
            interactionSource = interactionSource
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                ProvideTextStyle(value = MaterialTheme.typography.labelMedium) {
                    Row(
                        Modifier
                            .defaultMinSize(
                                minWidth = ButtonDefaults.MinWidth,
                                minHeight = 28.dp
                            )
                            .padding(contentPadding),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        content = content
                    )
                }
            }
        }
    }
}