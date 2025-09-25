package dev.zt64.tau.ui.theme

import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialTheme

@Composable
fun Theme(
    seedColor: ColorProducer,
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    DynamicMaterialTheme(
        seedColor = seedColor(),
        isDark = isDarkTheme,
        animate = true,
        animationSpec = tween()
    ) {
        CompositionLocalProvider(
            LocalContextMenuRepresentation provides DefaultContextMenuRepresentation(
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                itemHoverColor = MaterialTheme.colorScheme.inverseOnSurface
            ),
            LocalScrollbarStyle provides ScrollbarStyle(
                minimalHeight = 16.dp,
                thickness = 8.dp,
                shape = MaterialTheme.shapes.small,
                hoverDurationMillis = 300,
                unhoverColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f),
                hoverColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.50f)
            ),
            content = content
        )
    }
}