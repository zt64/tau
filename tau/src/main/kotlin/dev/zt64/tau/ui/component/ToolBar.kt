package dev.zt64.tau.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.zt64.tau.ui.state.BrowserState
import dev.zt64.ui.component.PathBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(state: BrowserState) {
    LaunchedEffect(state.search) {
        state.scanDir()
    }

    Surface(
        tonalElevation = 7.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilledIconButton(
                enabled = state.canGoUp,
                onClick = state::navigateUp,
            ) {
                Icon(Icons.Default.ArrowUpward, null)
            }

            FilledTonalIconButton(
                enabled = state.canGoBack,
                onClick = state::navigateBack,
            ) {
                Icon(Icons.Default.ArrowLeft, null)
            }

            FilledTonalIconButton(
                enabled = state.canGoForward,
                onClick = state::navigateForward,
            ) {
                Icon(Icons.Default.ArrowRight, null)
            }

            PathBar(
                state = state,
                modifier = Modifier.weight(1f, true),
                location = state.currentLocation,
                onClickSegment = state::navigate,
            )

            TextField(
                modifier = Modifier.heightIn(min = 42.dp, max = 42.dp),
                value = state.search,
                textStyle = MaterialTheme.typography.bodySmall,
                onValueChange = state::search::set,
                trailingIcon = {
                    Icon(Icons.Default.Search, null)
                },
                singleLine = true,
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            )
        }
    }
}