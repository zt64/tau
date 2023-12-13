package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun ParentWindow() {
    HorizontalSplitPane(
        modifier = Modifier.fillMaxWidth(),
        splitPaneState = rememberSplitPaneState(0.2f, moveEnabled = false)
    ) {
        first {
            Surface(
                modifier = Modifier.fillMaxSize(),
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ListItem(
                        modifier = Modifier.fillMaxWidth(),
                        headlineContent = {
                            Text(
                                text = "Hi"
                            )
                        }
                    )
                }
            }
        }
        second {
            AppearancePreferences()
        }
    }
}