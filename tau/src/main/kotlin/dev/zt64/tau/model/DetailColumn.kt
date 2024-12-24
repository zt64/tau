package dev.zt64.tau.model

import dev.zt64.tau.resources.*
import org.jetbrains.compose.resources.StringResource

enum class DetailColumnType(val displayName: StringResource) {
    NAME(Res.string.name),
    SIZE(Res.string.size),
    DATE_MODIFIED(Res.string.date_modified),
    DATE_CREATED(Res.string.date_created),
    DATE_ACCESSED(Res.string.date_accessed),
    PERMISSIONS(Res.string.permissions),
    OWNER(Res.string.owner),
    GROUP(Res.string.group),
    TYPE(Res.string.type)
}

data class DetailColumn(val type: DetailColumnType, val visible: Boolean)