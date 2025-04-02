package dev.zt64.tau.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.items
import dev.zt64.tau.resources.items_selected
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import dev.zt64.tau.util.humanReadableSize
import org.jetbrains.compose.resources.pluralStringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.io.path.name

@Composable
fun StatusBar(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<BrowserViewModel>()

    Surface(
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(36.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                Text(pluralStringResource(Res.plurals.items, viewModel.contents.size, viewModel.contents.size))

                when {
                    viewModel.selected.size == 1 -> {
                        viewModel.selected.single().let { selectedFile ->
                            Text(
                                text = selectedFile.name,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.width(16.dp))

                            Text("Size: ${selectedFile.humanReadableSize()}")
                        }
                    }
                    viewModel.selected.size > 1 -> {
                        Text(pluralStringResource(Res.plurals.items_selected, viewModel.selected.size, viewModel.selected.size))
                    }
                }
            }
        }
    }
}