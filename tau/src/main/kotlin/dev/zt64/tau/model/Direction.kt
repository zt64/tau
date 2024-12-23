package dev.zt64.tau.model

import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.sort_order_asc
import dev.zt64.tau.resources.sort_order_desc
import org.jetbrains.compose.resources.StringResource

enum class Direction(val s: StringResource) {
    ASCENDING(Res.string.sort_order_asc),
    DESCENDING(Res.string.sort_order_desc)
}