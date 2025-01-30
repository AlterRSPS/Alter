package dev.openrune.serialization
import dev.openrune.RSCMHandler
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


object RscmString : KSerializer<Int> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("RscmString", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeInt(value)
    }

    override fun deserialize(decoder: Decoder): Int {
        return when (val value = decoder.decodeStringOrInt()) {
            is String -> {
                RSCMHandler.getMapping(value) ?: value.toIntOrNull() ?: throw IllegalArgumentException("Invalid string value for Int conversion: $value")
            }
            is Int -> value
            else -> throw IllegalArgumentException("Unsupported type for Int conversion")
        }
    }
}

fun Decoder.decodeStringOrInt(): Any {
    return try {
        this.decodeInt()
    } catch (e: SerializationException) {
        this.decodeString()
    }
}
