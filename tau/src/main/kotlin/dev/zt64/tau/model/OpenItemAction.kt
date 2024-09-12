package dev.zt64.tau.model

import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.double_click
import dev.zt64.tau.resources.single_click
import org.jetbrains.compose.resources.StringResource

enum class OpenItemAction(val s: StringResource) {
    SINGLE_CLICK(Res.string.single_click),
    DOUBLE_CLICK(Res.string.double_click)
}