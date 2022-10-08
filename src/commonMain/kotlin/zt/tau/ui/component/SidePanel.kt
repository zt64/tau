package zt.tau.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SidePanel() {
    Surface(
        modifier = Modifier.fillMaxHeight(),
        tonalElevation = 2.dp
    ) {
        LazyColumn(
            modifier = Modifier.width(120.dp), // eventually fetch from settings
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

        }
    }
}