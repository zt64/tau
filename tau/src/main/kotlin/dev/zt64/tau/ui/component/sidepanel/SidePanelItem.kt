package dev.zt64.tau.ui.component.sidepanel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.NavigationManager
import dev.zt64.tau.model.Bookmark
import dev.zt64.tau.ui.component.menu.ItemContextMenu
import org.koin.compose.koinInject

@Composable
fun SidePanelItem(
    text: String,
    icon: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(Modifier.width(4.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun SidePanelLocationItem(
    data: Bookmark,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val navigationManager = koinInject<NavigationManager>()
    val scope = rememberCoroutineScope()

    ItemContextMenu(data.path) {
        SidePanelItem(
            text = data.displayName,
            icon = {
                Icon(
                    imageVector = data.icon,
                    contentDescription = null
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        )
    }
}

@Composable
fun SidePanelHeaderItem(
    text: String,
    modifier: Modifier = Modifier
) {
    SidePanelItem(
        text = text,
        modifier = modifier
    )
}