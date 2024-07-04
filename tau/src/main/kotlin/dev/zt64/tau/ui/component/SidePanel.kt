package dev.zt64.tau.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.model.Bookmark
import dev.zt64.tau.ui.component.sidepanel.Bookmark
import dev.zt64.tau.ui.state.BrowserState
import oshi.SystemInfo
import kotlin.io.path.Path

@Composable
fun SidePanel(state: BrowserState) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        tonalElevation = 5.dp
    ) {
        val roots = remember {
            SystemInfo().operatingSystem.fileSystem.getFileStores(true)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = roots,
                key = { it }
            ) {
                Bookmark(
                    data = Bookmark(Path(it.mount), it.label.ifEmpty { it.name }),
                    icon = Icons.Default.Storage,
                    onClick = { state.navigate(Path(it.mount)) }
                )
            }
        }
    }
}