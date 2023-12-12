package dev.zt64.tau.ui.window.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class, ExperimentalMaterialApi::class)
@Composable
fun ParentWindow() {

    HorizontalSplitPane(
        modifier = Modifier
            .fillMaxWidth(),
        splitPaneState = rememberSplitPaneState(0.2f)
    ) {
        first {
            Surface (
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ListItem(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Hi",
                            color = Color.Black
                        )
                    }
                }
            }
        }
        second {
            AppearancePreferences()
        }
    }
}