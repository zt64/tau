package dev.zt64.tau.ui.widget.toolbar

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.search
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Primary toolbar
 */
@Composable
fun Toolbar() {
    val viewModel = koinViewModel<BrowserViewModel>()

    val entries by remember {
        mutableStateOf(
            listOf(
                ToolbarEntry.Up,
                ToolbarEntry.Back,
                ToolbarEntry.Forward,
                ToolbarEntry.Separator,
                ToolbarEntry.PathBar,
                ToolbarEntry.Spacer,
                ToolbarEntry.Search
            )
        )
    }

    Surface(
        tonalElevation = 7.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var operations = remember {
                mutableStateListOf<Unit>()
            }

            entries.forEach { entry ->
                when (entry) {
                    ToolbarEntry.Spacer -> {
                        Spacer(Modifier.weight(1f))
                    }
                    ToolbarEntry.Separator -> {
                        VerticalDivider(
                            modifier = Modifier.height(24.dp),
                            thickness = 2.dp
                        )
                    }
                    is ToolbarEntry.PathBar -> {
                        Crossfade(
                            targetState = viewModel.searching,
                            animationSpec = tween(200)
                        ) {
                            if (false) {
                                val focusRequester = remember { FocusRequester() }

                                DisposableEffect(Unit) {
                                    focusRequester.requestFocus()

                                    onDispose {
                                        viewModel.clearSearch()
                                    }
                                }

                                SearchField(
                                    modifier = Modifier
                                        .focusRequester(focusRequester)
                                        .width(400.dp)
                                        .heightIn(min = 42.dp, max = 42.dp),
                                    value = viewModel.search,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    onValueChange = viewModel::search,
                                    placeholder = {
                                        Text(
                                            text = stringResource(Res.string.search),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                )
                            } else {
                                val currentLocation by viewModel.nav.currentLocation.collectAsState()
                                PathBar(
                                    modifier = Modifier.weight(1f),
                                    location = currentLocation,
                                    onClickSegment = viewModel::navigate
                                )
                            }
                        }
                    }
                    is ToolbarEntry.Back -> {
                        entry.Content(
                            enabled = viewModel.nav.canGoBack,
                            onClick = viewModel::navigateBack
                        )
                    }
                    is ToolbarEntry.Forward -> {
                        entry.Content(
                            enabled = viewModel.nav.canGoForward,
                            onClick = viewModel::navigateForward
                        )
                    }
                    is ToolbarEntry.Up -> {
                        entry.Content(
                            enabled = viewModel.nav.canGoUp,
                            onClick = viewModel::navigateUp
                        )
                    }
                    is ToolbarEntry.Refresh -> {
                        TODO()
                    }
                    is ToolbarEntry.Search -> {
                        FilledTonalIconToggleButton(
                            checked = viewModel.searching,
                            onCheckedChange = { viewModel.searching = it }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(Res.string.search)
                            )
                        }
                    }
                    is ToolbarEntry.AddBookmark -> {
                        entry.Content(
                            onClick = {}
                        )
                    }
                    is ToolbarEntry.CreateNew -> TODO()
                    is ToolbarEntry.Split -> TODO()
                    is ToolbarEntry.OpenMenu -> TODO()
                    is ToolbarEntry.NewTab -> TODO()
                    is ToolbarEntry.NewWindow -> TODO()
                }
            }

            if (operations.isNotEmpty()) {
                FilledTonalIconButton(
                    onClick = {
                    }
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )
) {
    val textColor = textStyle.color.takeOrElse {
        val focused = interactionSource.collectIsFocusedAsState().value
        colors.textColor(enabled, isError, focused)
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier = modifier.defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError)),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions.Default,
            interactionSource = interactionSource,
            singleLine = true,
            maxLines = 1,
            minLines = 1,
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.DecorationBox(
                    value = value,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    label = label,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    shape = CircleShape,
                    singleLine = true,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        )
    }
}