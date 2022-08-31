package zt.tau.ui.component.sidepanel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zt.tau.model.Bookmark

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Bookmark(
    data: Bookmark,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .combinedClickable(onClick = onClick)
            .padding(horizontal = 8.dp)
            .widthIn(min = 120.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(data.displayName)
    }
}