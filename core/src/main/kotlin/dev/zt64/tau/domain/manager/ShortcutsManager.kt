package dev.zt64.tau.domain.manager

import androidx.compose.ui.input.key.Key
import com.russhwolf.settings.PreferencesSettings
import dev.zt64.tau.domain.manager.base.BasePreferenceManager
import dev.zt64.tau.domain.model.KeyModifier
import dev.zt64.tau.domain.model.Shortcut

/**
 * Manages shortcuts
 */
class ShortcutsManager(settings: PreferencesSettings) : BasePreferenceManager(settings) {
    /**
     * List of all available shortcuts
     */
    val shortcuts = mutableListOf<Shortcut>()

    var selectAll by shortcut(Shortcut(KeyModifier.Ctrl, Key.A))
    var copy by shortcut(Shortcut(KeyModifier.Ctrl, Key.C))
    var cut by shortcut(Shortcut(KeyModifier.Ctrl, Key.X))
    var paste by shortcut(Shortcut(KeyModifier.Ctrl, Key.V))
    var delete by shortcut(Shortcut(Key.Delete))
    var rename by shortcut(Shortcut(Key.F2))
    var newFolder by shortcut(Shortcut(KeyModifier.Ctrl, Key.N))
    var newTab by shortcut(Shortcut(KeyModifier.Ctrl, Key.T))
    var toggleHidden by shortcut(Shortcut(KeyModifier.Ctrl, Key.H))

    var menu by shortcut(Shortcut(Key.AltLeft))

    private fun shortcut(defaultValue: Shortcut): PreferenceProvider<Shortcut> {
        shortcuts += defaultValue
        return PreferenceProvider(
            key = null,
            defaultValue = defaultValue,
            getter = { key, _ ->
                settings.getIntOrNull(key)?.let(::Shortcut) ?: defaultValue
            },
            setter = { key, newValue ->
                settings.putInt(key, newValue.packToInt())
            }
        )
    }
}