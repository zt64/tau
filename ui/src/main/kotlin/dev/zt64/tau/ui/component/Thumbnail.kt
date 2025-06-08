package dev.zt64.tau.ui.component

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import dev.zt64.tau.util.getThumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isHidden

@Composable
fun Thumbnail(path: Path, modifier: Modifier = Modifier.Companion) {
    val scope = rememberCoroutineScope()
    var bitmap by remember(path) { mutableStateOf<ImageBitmap?>(null) }
    val fallbackIcon = remember(path) {
        if (path.isDirectory()) Icons.Default.Folder else Icons.Default.Description
    }

    LaunchedEffect(path) {
        scope.launch(Dispatchers.IO) {
            try {
                bitmap = path.getThumbnail()
            } catch (e: Exception) {
                println("Error loading thumbnail: ${e.message}")
            }
        }
    }

    if (bitmap != null) {
        Image(
            modifier = modifier,
            bitmap = bitmap!!,
            contentDescription = null
        )
    } else {
        Icon(
            modifier = modifier,
            imageVector = fallbackIcon,
            tint = if (path.isHidden()) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.primary
            },
            contentDescription = null
        )
    }
}