package dev.zt64.tau.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import java.nio.file.Path
import kotlin.io.path.name

@Stable
data class Bookmark(
    val path: Path,
    val name: String = path.name,
    val icon: ImageVector = Icons.Default.Folder
) {
    var displayName by mutableStateOf(name)
}