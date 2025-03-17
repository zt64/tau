package dev.zt64.tau.ui.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ScrollableContainer(
    adapter: ScrollbarAdapter,
    content: @Composable BoxScope.() -> Unit
) {
    Box {
        content()

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = adapter
        )
    }
}

@Composable
fun ScrollableContainer(
    state: ScrollState,
    content: @Composable BoxScope.() -> Unit
) {
    ScrollableContainer(
        adapter = rememberScrollbarAdapter(state),
        content = content
    )
}

@Composable
fun ScrollableContainer(
    state: LazyListState,
    content: @Composable BoxScope.() -> Unit
) {
    ScrollableContainer(
        adapter = rememberScrollbarAdapter(state),
        content = content
    )
}

@Composable
fun ScrollableContainer(
    state: LazyGridState,
    content: @Composable BoxScope.() -> Unit
) {
    ScrollableContainer(
        adapter = rememberScrollbarAdapter(state),
        content = content
    )
}