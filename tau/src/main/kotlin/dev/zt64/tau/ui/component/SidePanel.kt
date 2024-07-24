package dev.zt64.tau.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.model.Bookmark
import dev.zt64.tau.ui.component.sidepanel.Bookmark
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import oshi.SystemInfo
import kotlin.io.path.Path

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SidePanel() {
    val viewModel = koinViewModel<BrowserViewModel>()

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        tonalElevation = 5.dp
    ) {
        val roots = remember {
            SystemInfo().operatingSystem.fileSystem.getFileStores(false)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = roots,
                key = { it }
            ) {
                Bookmark(
                    data = Bookmark(
                        Path(it.mount),
                        "${it.label.ifEmpty { it.description }} (${it.mount})"
                    ),
                    icon = when (it.description.lowercase()) {
                        "removable drive" -> Icons.Default.Usb
                        "fixed drive" -> Icons.Default.Storage
                        "local disk" -> Icons.Default.Storage
                        // only linux seems to do this one?
                        "network drive" -> Icons.Default.Lan
                        "ram disk" -> Icons.Default.Memory
                        "mount point" -> Icons.Default.Folder
                        else -> Icons.Default.DisabledByDefault
                    },
                    onClick = { viewModel.navigate(Path(it.mount)) }
                )
            }
        }
    }
}