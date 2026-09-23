package dev.zt64.tau.domain.model

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object KeySerializer : KSerializer<Key> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Key", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Key) {
        encoder.encodeLong(value.keyCode)
    }

    override fun deserialize(decoder: Decoder): Key {
        return Key(decoder.decodeLong())
    }
}

/**
 * Modifier represents a modifier key that can be used in a shortcut.
 *
 */
@Serializable
enum class KeyModifier {
    Ctrl,
    Alt,
    Shift,

    /**
     * Meta is the command key on macOS and the windows key on Windows.
     */
    Meta
}

@Serializable(with = ShortcutSerializer::class)
sealed interface Shortcut {
    fun packToInt(): Int

    @Serializable
    data class Bound(
        val modifiers: List<KeyModifier>,
        @Serializable(with = KeySerializer::class) val key: Key
    ) : Shortcut {
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

    @Serializable
    data object Unbound : Shortcut {
        override fun packToInt(): Int = 0
    }
}

object ShortcutSerializer : KSerializer<Shortcut> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Shortcut", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Shortcut) {
        encoder.encodeInt(value.packToInt())
    }

    override fun deserialize(decoder: Decoder): Shortcut {
        return Shortcut(decoder.decodeInt())
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