package zt.tau.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.russhwolf.settings.Settings
import zt.tau.ui.window.colorTheme

val settings: Settings = Settings()

@Composable
fun TauTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = when (colorTheme) {
            "dark" -> darkColorScheme()
            "light" -> lightColorScheme()
            else -> darkColorScheme()
        },
        content = content
    )
}

