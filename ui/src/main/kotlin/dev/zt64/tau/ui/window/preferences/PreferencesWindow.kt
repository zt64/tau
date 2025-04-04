package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Shortcut
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import dev.zt64.tau.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension

private enum class SettingsPage(
    val label: StringResource,
    val icon: ImageVector,
    val content: @Composable () -> Unit
) {
    APPEARANCE(Res.string.appearance, Icons.Default.Palette, { AppearancePreferences() }),
    BEHAVIOR(Res.string.behavior, Icons.Default.Settings, { BehaviorPreferences() }),
    SHORTCUTS(Res.string.shortcuts, Icons.AutoMirrored.Default.Shortcut, { ShortcutsPreferences() })
}

@Composable
fun PreferencesWindow(onCloseRequest: () -> Unit) {
    Window(
        onCloseRequest = onCloseRequest,
        title = stringResource(Res.string.settings),
        resizable = true,
        icon = painterResource(Res.drawable.window_icon)
    ) {
        LaunchedEffect(Unit) {
            window.minimumSize = Dimension(540, 300)
        }

        var selectedCategory by remember {
            mutableStateOf(SettingsPage.APPEARANCE)
        }

        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    modifier = Modifier.width(240.dp)
                ) {
                    SettingsPage.entries.forEach {
                        NavigationDrawerItem(
                            icon = { Icon(it.icon, null) },
                            label = { Text(stringResource(it.label)) },
                            selected = selectedCategory == it,
                            onClick = { selectedCategory = it },
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                end = 16.dp,
                                bottom = 0.dp
                            )
                        )
                    }
                }
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = selectedCategory.content
            )
        }
    }
}

@Composable
fun Placeholder() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) { }
}