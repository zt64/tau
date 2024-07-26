package dev.zt64.tau.domain.manager

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import com.russhwolf.settings.PreferencesSettings
import dev.zt64.tau.domain.manager.base.BasePreferenceManager

class ShortcutsManager(settings: PreferencesSettings) : BasePreferenceManager(settings) {
    var selectAll by preference(Shortcut(Key.CtrlLeft, Key.A))
    var copy by preference(Shortcut(Key.CtrlLeft, Key.C))
    var cut by preference(Shortcut(Key.CtrlLeft, Key.X))
    var paste by preference(Shortcut(Key.CtrlLeft, Key.V))
    var delete by preference(Shortcut(Key.Delete))
    var rename by preference(Shortcut(Key.F2))
    var newFolder by preference(Shortcut(Key.CtrlLeft, Key.N))

    var menu by preference(Shortcut(Key.AltLeft))

    private fun preference(defaultValue: Shortcut) = preference(null, defaultValue)

    private fun preference(
        key: String?,
        defaultValue: Shortcut
    ) = PreferenceProvider(
        key = key,
        defaultValue = defaultValue,
        getter = { key, defaultValue ->
            settings.getIntOrNull(key)?.let(::Shortcut) ?: defaultValue
        },
        setter = { key, newValue ->
            settings.putInt(key, newValue.packToInt())
        }
    )
}

sealed interface Shortcut {
    fun packToInt(): Int

    data class Bound(val modifier: Key, val key: Key) : Shortcut {
        constructor(packed: Int) : this(
            Key(packed),
            Key(packed shr 32)
        )

        override fun packToInt(): Int {
            return modifier.nativeKeyCode shl 32 or key.nativeKeyCode
        }
    }

    data class Single(val key: Key) : Shortcut {
        constructor(packed: Int) : this(Key(packed))

        override fun packToInt(): Int {
            return key.nativeKeyCode
        }
    }

    data object Unbound : Shortcut {
        override fun packToInt(): Int = 0
    }
}

private fun Shortcut(packed: Int): Shortcut = when (packed) {
    0 -> Shortcut.Unbound
    packed and 0xFFFFFFF -> Shortcut.Single(packed)
    else -> Shortcut.Bound(packed)
}

private fun Shortcut(
    modifier: Key,
    key: Key
): Shortcut = Shortcut.Bound(modifier, key)

private fun Shortcut(key: Key): Shortcut = Shortcut.Single(key)