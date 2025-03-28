package dev.zt64.tau.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.view_mode_icons
import dev.zt64.tau.resources.view_mode_list
import org.jetbrains.compose.resources.StringResource

@Stable
enum class ViewMode(val label: StringResource, val icon: ImageVector) {
    LIST(Res.string.view_mode_list, Icons.AutoMirrored.Default.List),
    GRID(Res.string.view_mode_icons, Icons.Default.Grid4x4)
}