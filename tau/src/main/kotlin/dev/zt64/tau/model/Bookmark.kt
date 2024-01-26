package dev.zt64.tau.model

import androidx.compose.runtime.*
import java.nio.file.Path
import kotlin.io.path.name

@Stable
data class Bookmark(
    val path: Path,
    val name: String = path.name
) {
    var displayName by mutableStateOf(name)
}