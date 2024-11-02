package dev.zt64.tau.util

import dev.zt64.tau.resources.Res
import dev.zt64.tau.resources.sort_order_asc
import dev.zt64.tau.resources.sort_order_desc
import dev.zt64.tau.resources.sort_type_creation_date
import dev.zt64.tau.resources.sort_type_modification_date
import dev.zt64.tau.resources.sort_type_name
import dev.zt64.tau.resources.sort_type_size
import org.jetbrains.compose.resources.StringResource

enum class SortType(val s: StringResource) {
    NAME(Res.string.sort_type_name),
    SIZE(Res.string.sort_type_size),
    CREATION_DATE(Res.string.sort_type_creation_date),
    MODIFICATION_DATE(Res.string.sort_type_modification_date)
}

enum class SortDirection(val s: StringResource) {
    ASCENDING(Res.string.sort_order_asc),
    DESCENDING(Res.string.sort_order_desc)
}