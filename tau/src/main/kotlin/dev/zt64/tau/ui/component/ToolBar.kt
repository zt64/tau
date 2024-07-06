package dev.zt64.tau.ui.component

import Res
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import dev.zt64.tau.domain.manager.PreferencesManager
import dev.zt64.tau.ui.state.BrowserState
import org.koin.compose.koinInject

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun Toolbar(state: BrowserState) {
    val preferencesManager = koinInject<PreferencesManager>()

    LaunchedEffect(state.search) {
        state.scanDir(preferencesManager.showHiddenFiles)
    }

    Surface(
        tonalElevation = 7.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var searching by rememberSaveable { mutableStateOf(false) }

            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                state = rememberTooltipState(),
                tooltip = {
                    PlainTooltip {
                        Text(Res.string.goto_parent_folder)
                    }
                }
            ) {
                FilledIconButton(
                    enabled = state.canGoUp,
                    onClick = state::navigateUp
                ) {
                    Icon(Icons.Default.ArrowUpward, null)
                }
            }

            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                state = rememberTooltipState(),
                tooltip = {
                    PlainTooltip {
                        Text(Res.string.back)
                    }
                }
            ) {
                FilledTonalIconButton(
                    enabled = state.canGoBack,
                    onClick = state::navigateBack
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowLeft, Res.string.back)
                }
            }

            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                state = rememberTooltipState(),
                tooltip = {
                    PlainTooltip {
                        Text(Res.string.forward)
                    }
                }
            ) {
                FilledTonalIconButton(
                    enabled = state.canGoForward,
                    onClick = state::navigateForward
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowRight, Res.string.forward)
                }
            }

            Crossfade(
                targetState = searching,
                animationSpec = tween(200)
            ) {
                if (it) {
                    DisposableEffect(Unit) {
                        onDispose {
                            state.search = ""
                        }
                    }

                    SearchField(
                        modifier = Modifier
                            .width(400.dp)
                            .heightIn(min = 42.dp, max = 42.dp),
                        value = state.search,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        onValueChange = state::search::set,
                        placeholder = {
                            Text(
                                text = Res.string.search,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
                } else {
                    PathBar(
                        state = state,
                        modifier = Modifier.weight(1f, true),
                        location = state.currentLocation,
                        onClickSegment = state::navigate
                    )
                }
            }

            Spacer(Modifier.weight(1f, true))

            FilledTonalIconToggleButton(
                checked = searching,
                onCheckedChange = { searching = it }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = Res.string.search
                )
            }
        }
    }
}

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
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
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
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