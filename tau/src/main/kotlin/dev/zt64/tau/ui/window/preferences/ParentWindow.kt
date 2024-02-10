package dev.zt64.tau.ui.window.preferences

import Res
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private enum class SettingsPage(
    val label: String,
    val icon: ImageVector
) {
    APPEARANCE(Res.string.appearance, Icons.Default.Palette),
    BEHAVIOR(Res.string.behavior, Icons.Default.Settings)
}

@Composable
fun ParentWindow() {
    var selectedCategory by remember {
        mutableStateOf(SettingsPage.APPEARANCE)
    }

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(240.dp)
            ) {
                SettingsPage.values().forEach {
                    NavigationDrawerItem(
                        icon = { Icon(it.icon, "") },
                        label = { Text(text = it.label) },
                        selected = selectedCategory == it,
                        onClick = {
                            selectedCategory = it
                        },
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = 16.dp,
                                top = 16.dp,
                                end = 16.dp,
                                bottom = 0.dp
                            )
                        )
                    )
                }
            }
        }
    ) {
        when (selectedCategory) { // TODO: there has to be a better way without hardcoding :sob:
            SettingsPage.APPEARANCE -> AppearancePreferences()
            SettingsPage.BEHAVIOR -> BehaviorPreferences()
        }
    }
}

@Composable
fun Placeholder() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) { }
}