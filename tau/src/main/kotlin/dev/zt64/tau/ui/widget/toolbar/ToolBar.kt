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
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowUpward
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
import dev.zt64.tau.resources.*
import dev.zt64.tau.ui.component.tooltip.PlainTooltipBox
import dev.zt64.tau.ui.viewmodel.BrowserViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Primary toolbar
 */
@Composable
fun Toolbar() {
    val viewModel = koinViewModel<BrowserViewModel>()

    Surface(
        tonalElevation = 7.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var operations = remember {
                mutableStateListOf<Unit>()
            }

            PlainTooltipBox(
                tooltipContent = {
                    Text(stringResource(Res.string.goto_parent_folder))
                }
            ) {
                FilledIconButton(
                    enabled = viewModel.canGoUp,
                    onClick = viewModel::navigateUp
                ) {
                    Icon(Icons.Default.ArrowUpward, null)
                }
            }

            PlainTooltipBox(
                tooltipContent = {
                    Text(stringResource(Res.string.back))
                }
            ) {
                FilledTonalIconButton(
                    enabled = viewModel.canGoBack,
                    onClick = viewModel::navigateBack
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowLeft, stringResource(Res.string.back))
                }
            }

            PlainTooltipBox(
                tooltipContent = {
                    Text(stringResource(Res.string.forward))
                }
            ) {
                FilledTonalIconButton(
                    enabled = viewModel.canGoForward,
                    onClick = viewModel::navigateForward
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowRight, stringResource(Res.string.forward))
                }
            }

            Crossfade(
                targetState = viewModel.searching,
                animationSpec = tween(200)
            ) {
                if (it) {
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
                    PathBar(
                        modifier = Modifier.weight(1f),
                        location = viewModel.currentLocation,
                        onClickSegment = viewModel::navigate
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            FilledTonalIconToggleButton(
                checked = viewModel.searching,
                onCheckedChange = { viewModel.searching = it }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(Res.string.search)
                )
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
            modifier = modifier
                .defaultMinSize(
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