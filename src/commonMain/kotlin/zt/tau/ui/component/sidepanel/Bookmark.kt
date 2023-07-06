package zt.tau.ui.component.sidepanel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import zt.tau.model.Bookmark

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Bookmark(
    data: Bookmark,
    onClick: () -> Unit,
    icon: ImageVector = Icons.Default.Folder,
) {
    ListItem(
        modifier = Modifier
            .combinedClickable(onClick = onClick)
            .padding(horizontal = 4.dp)
            .widthIn(min = 120.dp),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        headlineText = {
            Text(data.displayName)
        }
    )
}