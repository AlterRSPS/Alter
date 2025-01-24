package dev.openrune.serialization

import dev.openrune.RSCMHandler
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object RscmList : KSerializer<List<Any>> { // Changed to List<Any>
    @OptIn(InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("RscmValue", StructureKind.LIST)

    override fun serialize(encoder: Encoder, value: List<Any>) { // Changed to List<Any>
        val listEncoder = encoder.beginCollection(descriptor, value.size)
        for ((index, item) in value.withIndex()) {
            when (item) {
                is Int -> listEncoder.encodeIntElement(descriptor, index, item)
                is String -> listEncoder.encodeStringElement(descriptor, index, item)
                else -> throw SerializationException("List contains unsupported type: ${item?.javaClass?.simpleName}")
            }
        }
        listEncoder.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): List<Any> { // Changed to List<Any>
        val compositeDecoder = decoder.beginStructure(descriptor)
        val list = mutableListOf<Any>()

        while (true) {
            val index = compositeDecoder.decodeElementIndex(descriptor)
            if (index == CompositeDecoder.DECODE_DONE) break

            try {
                // Attempt to decode as Int
                list.add(compositeDecoder.decodeIntElement(descriptor, index))
            } catch (e: SerializationException) {
                // If it fails, try decoding as String
                val value = compositeDecoder.decodeStringElement(descriptor, index)
                // Lookup in the map or convert to Int
                val intValue = RSCMHandler.getMapping(value) ?: value.toIntOrNull()
                if (intValue != null) {
                    list.add(intValue)
                } else {
                    list.add(value)  // Add the String as is if conversion fails
                }
            }
        }
        compositeDecoder.endStructure(descriptor)

        return list // Return the list directly
    }
}
