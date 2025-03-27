package dev.zt64.tau.model

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode

/**
 * Modifier represents a modifier key that can be used in a shortcut.
 *
 */
enum class KeyModifier {
    Ctrl,
    Alt,
    Shift,

    /**
     * Meta is the command key on macOS and the windows key on Windows.
     */
    Meta
}

sealed interface Shortcut {
    fun packToInt(): Int

    data class Bound(val modifiers: List<KeyModifier>, val key: Key) : Shortcut {
        constructor(key: Key) : this(emptyList(), key)

        constructor(packed: Int) : this(
            (0 until 4).mapNotNull { index ->
                if (packed and (1 shl index) != 0) KeyModifier.entries[index] else null
            },
            Key(packed ushr 32)
        )

        override fun packToInt(): Int {
            return key.nativeKeyCode shl 32 or modifiers.fold(0) { acc, modifier ->
                acc or (1 shl modifier.ordinal)
            }
        }
    }

    data object Unbound : Shortcut {
        override fun packToInt(): Int = 0
    }
}

fun Shortcut(packed: Int): Shortcut = when (packed) {
    0 -> Shortcut.Unbound
    else -> Shortcut.Bound(packed)
}

fun Shortcut(
    modifier: List<KeyModifier>,
    key: Key
): Shortcut = Shortcut.Bound(modifier, key)

fun Shortcut(
    modifier: KeyModifier,
    key: Key
): Shortcut = Shortcut.Bound(listOf(modifier), key)

fun Shortcut(key: Key): Shortcut = Shortcut.Bound(key)