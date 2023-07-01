package zt.tau.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

data class Bookmark(val path: Path, val name: String = path.name) {
    var displayName by mutableStateOf(name)
}