package dev.zt64.tau.domain.manager

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class NotificationEvent(
    val message: String,
    val action: NotificationAction? = null
)

data class NotificationAction(
    val label: String,
    val action: () -> Unit,
)

class NotificationManager {
    private val _events = Channel<NotificationEvent>()
    val events = _events.receiveAsFlow()

    suspend fun notify(message: String, action: NotificationAction? = null) {
        _events.send(NotificationEvent(message, action))
    }
}