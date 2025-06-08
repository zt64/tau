package dev.zt64.tau.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.domain.model.OpenItemAction
import dev.zt64.tau.ui.component.menu.ItemContextMenu
import dev.zt64.tau.ui.window.PropertiesWindow
import org.koin.compose.koinInject
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.File
import java.nio.file.Path
import kotlin.io.path.isReadable
import kotlin.io.path.isSymbolicLink
import kotlin.io.path.name

@Composable
fun FileItem(
    path: Path,
    selected: Boolean,
    onSelect: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    var showProperties by remember { mutableStateOf(false) }
    val preferencesManager = koinInject<PreferencesManager>()

    if (showProperties) {
        PropertiesWindow(
            path = path,
            onCloseRequest = { showProperties = false }
        )
    }

    val clipboard = LocalClipboard.current

    ItemContextMenu(path) {
        TooltipArea(
            tooltip = {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 6.dp,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = path.name
                    )
                }
            }
        ) {
            val currentAction by rememberUpdatedState(preferencesManager.openItemAction)

            val onClickLambda = remember(currentAction) {
                when (currentAction) {
                    OpenItemAction.SINGLE_CLICK -> onOpen
                    OpenItemAction.DOUBLE_CLICK -> onSelect
                }
            }

            val onDoubleClickLambda = remember(currentAction) {
                when (currentAction) {
                    OpenItemAction.SINGLE_CLICK -> onSelect
                    OpenItemAction.DOUBLE_CLICK -> onOpen
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        onClick = onClickLambda,
                        onDoubleClick = onDoubleClickLambda
                    )
                    .semantics { this.selected = selected }
                    .background(
                        if (selected) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .then(modifier),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Thumbnail(
                        modifier = Modifier.size(48.dp),
                        path = path
                    )

                    val icon = when {
                        path.isSymbolicLink() -> Icons.Outlined.Link
                        !path.isReadable() -> Icons.Outlined.Lock
                        else -> null
                    }

                    if (icon != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize(0.3f)
                                .align(Alignment.BottomEnd),
                            color = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null
                            )
                        }
                    }
                }

                var fileName by remember(path) {
                    mutableStateOf(path.name)
                }

                var renaming by remember { mutableStateOf(true) }

                BasicTextField(
                    readOnly = !renaming,
                    value = fileName,
                    // textAlign = TextAlign.Center,
                    // overflow = if (preferencesManager.truncateNames) {
                    //     TextOverflow.Ellipsis
                    // } else {
                    //     TextOverflow.Visible
                    // },
                    maxLines = if (preferencesManager.truncateNames && !renaming) {
                        preferencesManager.maxNameLines
                    } else {
                        Int.MAX_VALUE
                    },
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = LocalContentColor.current,
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.outline,
                            offset = Offset.Zero,
                            blurRadius = 0.75f
                        ),
                        textAlign = TextAlign.Center
                    ),
                    onValueChange = {
                        // ensure that the file name is valid
                        if (it.contains(Regex("[\\\\/:*?\"<>|]"))) {
                            fileName = it
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun FileItemPreview() {
    FileItem(
        path = File("/home/tau").toPath(),
        selected = false,
        onSelect = {},
        onOpen = {}
    )
}

data class FileTransferable(private val listOfFiles: List<File>) : Transferable {
    override fun getTransferDataFlavors() = arrayOf(DataFlavor.javaFileListFlavor)

    override fun isDataFlavorSupported(flavor: DataFlavor) = DataFlavor.javaFileListFlavor == flavor

    override fun getTransferData(flavor: DataFlavor) = listOfFiles
}