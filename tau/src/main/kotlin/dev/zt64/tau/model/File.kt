package dev.zt64.tau.model

import androidx.compose.runtime.Stable
import java.nio.file.Path

@Stable
data class File(
    val path: Path,
    val name: String,
    val isDirectory: Boolean,
    val isHidden: Boolean,
    val size: Long,
    val lastModified: Long
)