package zt.tau.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zt.tau.ui.theme.settings
import zt.tau.ui.window.colorTheme
import androidx.compose.ui.window.MenuScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScope.ThemeCheckboxItem(
    themeName: String,
    modifier: Modifier = Modifier
) {
    val lower = themeName.lowercase()

    CheckboxItem(
        themeName,
        checked = colorTheme == lower,
        onCheckedChange = {
            settings.putString("colorScheme", lower)
            colorTheme = lower
        }
    )
}

