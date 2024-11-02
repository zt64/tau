package dev.zt64.tau.model

import dev.zt64.tau.resources.*
import org.jetbrains.compose.resources.StringResource

sealed interface Sort {
    val label: StringResource

    /**
     * General sort which apply to all kinds of files
     */
    enum class General(override val label: StringResource) : Sort {
        NAME(Res.string.name),
        SIZE(Res.string.size),
        TYPE(Res.string.type),
        DATE_CREATED(Res.string.date_created),
        DATE_ACCESSED(Res.string.date_accessed),
        DATE_MODIFIED(Res.string.date_modified)
    }

    enum class Image(override val label: StringResource) : Sort

    enum class Audio(override val label: StringResource) : Sort {
        BITRATE(Res.string.bitrate)
    }

    enum class Video(override val label: StringResource) : Sort {
        FRAME_RATE(Res.string.frame_rate)
    }
}

