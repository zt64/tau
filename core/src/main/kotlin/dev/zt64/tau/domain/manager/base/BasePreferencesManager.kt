package dev.zt64.tau.domain.manager.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import dev.zt64.tau.domain.manager.Shortcuts
import dev.zt64.tau.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings = Settings()

    override suspend fun readFrom(input: InputStream): Settings = try {
        Json.decodeFromString<Settings>(input.readBytes().decodeToString())
    } catch (serialization: SerializationException) {
        throw CorruptionException("Unable to read Settings", serialization)
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(t)
                    .encodeToByteArray()
            )
        }
    }
}

@Serializable
data class Settings(
    val appearance: AppearanceSettings = AppearanceSettings(),
    val behavior: BehaviorSettings = BehaviorSettings(),
    val view: ViewSettings = ViewSettings(),
    val shortcuts: Shortcuts = Shortcuts()
) {
    @Serializable
    data class AppearanceSettings(
        val theme: Theme = Theme.DARK,
        val color: Long = 0x1ed760FFL,
        val scale: Int = 78,
        val showMenuBar: Boolean = true,
        val showToolbar: Boolean = true,
        val showStatusBar: Boolean = true,
        val maxNameLines: Int = 2,
        val truncateNames: Boolean = true
    )

    @Serializable
    data class BehaviorSettings(
        val openItemAction: OpenItemAction = OpenItemAction.DOUBLE_CLICK,
        val showHiddenFiles: Boolean = false,
        val confirmEmptyTrash: Boolean = true,
        val confirmDeleteItems: Boolean = true,
        val confirmExitWithTabs: Boolean = true
    )

    @Serializable
    data class ViewSettings(
        val viewMode: ViewMode = ViewMode.GRID,
        val sortType: DetailColumnType = DetailColumnType.NAME,
        val sortDirection: Direction = Direction.DESCENDING,
        val autoSizeColumns: Boolean = true
    )
}

/**
 * Base class for managing preferences.
 *
 * @property settings
 */
@Suppress("SameParameterValue", "MemberVisibilityCanBePrivate")
abstract class BasePreferenceManager(protected val settings: DataStore<Settings>) {
    fun <T> preference(getter: (Settings) -> T, defaultValue: T): Pref<T> {
        return Pref(getter, defaultValue)
    }

    inner class Pref<T>(val getter: (Settings) -> T, val defaultValue: T) {
        operator fun invoke() = settings.data.map(getter)

        suspend fun update(block: (Settings) -> Settings) {
            settings.updateData(block)
        }

        @Composable
        fun collectAsState(): State<T> {
            return invoke().collectAsState(defaultValue)
        }
    }

    // protected fun preference(
    //     key: String?,
    //     defaultValue: String
    // ) = PreferenceProvider(
    //     key = key,
    //     preferenceKey = ::stringPreferencesKey,
    //     defaultValue = defaultValue
    // )
    //
    // protected fun preference(defaultValue: String) = preference(null, defaultValue)
    //
    // protected fun preference(
    //     key: String?,
    //     defaultValue: Boolean
    // ) = PreferenceProvider(
    //     key = key,
    //     preferenceKey = ::booleanPreferencesKey,
    //     defaultValue = defaultValue
    // )
    //
    // protected fun preference(defaultValue: Boolean) = preference(null, defaultValue)
    //
    // protected fun preference(
    //     key: String?,
    //     defaultValue: Int
    // ) = PreferenceProvider(
    //     key = key,
    //     preferenceKey = ::intPreferencesKey,
    //     defaultValue = defaultValue
    // )
    //
    // protected fun preference(defaultValue: Int) = preference(null, defaultValue)
    //
    // protected fun preference(
    //     key: String?,
    //     defaultValue: Float
    // ) = PreferenceProvider(
    //     key = key,
    //     preferenceKey = ::floatPreferencesKey,
    //     defaultValue = defaultValue
    // )
    //
    // protected fun preference(defaultValue: Float) = preference(null, defaultValue)
    //
    // protected fun preference(
    //     key: String?,
    //     defaultValue: Long
    // ) = PreferenceProvider(
    //     key = key,
    //     preferenceKey = ::longPreferencesKey,
    //     defaultValue = defaultValue
    // )
    //
    // protected fun preference(defaultValue: Long) = preference(null, defaultValue)
    //
    // protected inline fun <reified E : Enum<E>> preference(
    //     key: String?,
    //     defaultValue: E
    // ) = EnumPreferenceProvider(
    //     key = key,
    //     defaultValue = defaultValue
    // )
    //
    // protected inline fun <reified E : Enum<E>> preference(defaultValue: E): EnumPreferenceProvider<E> {
    //     return preference(null, defaultValue)
    // }
    //
    // inner class Preference<T : Any>(private val key: Preferences.Key<T>, val defaultValue: T) {
    //     operator fun invoke() = settings.data.map { preferences ->
    //         preferences[key] ?: defaultValue
    //     }
    //
    //     @Composable
    //     fun collectAsState() = invoke().collectAsState(defaultValue)
    //
    //     suspend fun setValue(value: T) {
    //         settings.updateData {
    //             it.toMutablePreferences().also { prefs ->
    //                 prefs[key] = value
    //             }
    //         }
    //     }
    // }
    //
    // /**
    //  * Provides a delegate for a property that is backed by a preference.
    //  *
    //  * @param T
    //  * @property key
    //  * @property defaultValue
    //  */
    // protected inner class PreferenceProvider<T : Any>(
    //     private val key: String?,
    //     private val preferenceKey: (key: String) -> Preferences.Key<T>,
    //     private val defaultValue: T
    // ) {
    //     operator fun provideDelegate(
    //         thisRef: Any,
    //         property: KProperty<*>
    //     ) = ReadOnlyProperty<Any, Preference<T>> { _, property -> Preference(preferenceKey(key ?: property.name), defaultValue) }
    // }
    //
    // /**
    //  * Provides a delegate for enum preferences that are stored as Int ordinals.
    //  *
    //  * @param E The enum type
    //  * @property key The preference key (optional, uses property name if null)
    //  * @property defaultValue The default enum value
    //  */
    // protected inner class EnumPreferenceProvider<E : Enum<E>>(
    //     private val key: String?,
    //     private val defaultValue: E
    // ) {
    //     operator fun provideDelegate(
    //         thisRef: Any,
    //         property: KProperty<*>
    //     ): ReadOnlyProperty<Any, EnumPreference<E>> {
    //         val enumValues = enumEntries<E>()
    //         return ReadOnlyProperty { _, _ ->
    //             EnumPreference(
    //                 intPreferencesKey(key ?: property.name),
    //                 defaultValue,
    //                 enumValues
    //             )
    //         }
    //     }
    // }
    //
    // /**
    //  * A preference that stores an enum as an Int ordinal.
    //  *
    //  * @param E The enum type
    //  * @property key The preferences key
    //  * @property defaultValue The default enum value
    //  */
    // inner class EnumPreference<E : Enum<E>>(
    //     private val key: Preferences.Key<Int>,
    //     val defaultValue: E,
    //     private val enumValues: List<E>
    // ) {
    //     operator fun invoke() = settings.data.map { preferences ->
    //         val ordinal = preferences[key] ?: defaultValue.ordinal
    //         enumValues[ordinal]
    //     }
    //
    //     @Composable
    //     fun collectAsState() = invoke().collectAsState(defaultValue)
    //
    //     suspend fun setValue(value: E) {
    //         settings.updateData {
    //             it.toMutablePreferences().also { prefs ->
    //                 prefs[key] = value.ordinal
    //             }
    //         }
    //     }
    // }
    //
    // // fun clear() = settings.clear()
}