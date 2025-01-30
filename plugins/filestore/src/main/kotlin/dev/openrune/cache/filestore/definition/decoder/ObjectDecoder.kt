package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.CONFIGS
import dev.openrune.cache.CacheManager.revisionIsOrAfter
import dev.openrune.cache.OBJECT
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.ObjectType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.util.stream.IntStream
import kotlin.streams.toList

class ObjectDecoder : DefinitionDecoder<ObjectType>(CONFIGS) {

    override fun getArchive(id: Int) = OBJECT

    override fun create(): Int2ObjectOpenHashMap<ObjectType> = createMap { ObjectType(it) }

    override fun getFile(id: Int) = id

    override fun ObjectType.read(opcode: Int, buffer: Reader) {
        when (opcode) {
            1 -> {
                val length: Int = buffer.readUnsignedByte()
                when {
                    length > 0 -> {
                        objectTypes = MutableList(length) { 0 }
                        objectModels = MutableList(length) { 0 }

                        (0 until length).forEach {
                            objectModels!![it] = buffer.readUnsignedShort()
                            objectTypes!![it] = buffer.readUnsignedByte()
                        }
                    }
                }
            }
            2 -> name = buffer.readString()
            5 -> {
                val length: Int = buffer.readUnsignedByte()
                when {
                    length > 0 -> {
                        objectTypes = null
                        objectModels = IntStream.range(0, length).map {
                            buffer.readUnsignedShort()
                        }.toList().toMutableList()
                    }
                }
            }
            14 -> sizeX = buffer.readUnsignedByte()
            15 -> sizeY = buffer.readUnsignedByte()
            17 -> {
                solid = 0
                impenetrable = false
            }
            18 -> impenetrable = false
            19 -> interactive = buffer.readUnsignedByte()
            21 -> clipType = 0
            22 -> nonFlatShading = true
            23 -> modelClipped = true
            24 -> {
                animationId = buffer.readUnsignedShort()
                if (animationId == 65535) {
                    animationId = -1
                }
            }
            27 -> solid = 1
            28 -> decorDisplacement = buffer.readUnsignedByte()
            29 -> ambient = buffer.readByte()
            39 -> contrast = buffer.readByte()
            in 30..34 -> {
                actions[opcode - 30] = buffer.readString()
            }
            40 -> readColours(buffer)
            41 -> readTextures(buffer)
            61 -> category = buffer.readUnsignedShort()
            62 -> isRotated = true
            64 -> clipped = false
            65 -> modelSizeX = buffer.readUnsignedShort()
            66 -> modelSizeZ = buffer.readUnsignedShort()
            67 -> modelSizeY = buffer.readUnsignedShort()
            68 -> mapSceneID = buffer.readUnsignedShort()
            69 -> clipMask = buffer.readByte()
            70 -> offsetX = buffer.readUnsignedShort()
            71 -> offsetZ = buffer.readUnsignedShort()
            72 -> offsetY = buffer.readUnsignedShort()
            73 -> obstructive = true
            74 -> isHollow = true
            75 -> supportsItems = buffer.readUnsignedByte()
            77, 92 -> readTransforms(buffer, opcode == 92)
            78 -> {
                ambientSoundId = buffer.readUnsignedShort()
                soundDistance = buffer.readUnsignedByte()
                if (revisionIsOrAfter(220)) {
                    soundRetain = buffer.readUnsignedByte()
                }
            }
            79 -> {
                soundMin = buffer.readUnsignedShort()
                soundMax = buffer.readUnsignedShort()
                soundDistance = buffer.readUnsignedByte()
                if (revisionIsOrAfter(220)) {
                    soundRetain = buffer.readUnsignedByte()
                }
                val length: Int = buffer.readUnsignedByte()
                ambientSoundIds = IntStream.range(0, length).map {
                    buffer.readUnsignedShort()
                }.toList().toMutableList()
            }
            81 -> clipType = (buffer.readUnsignedByte()) * 256
            60,82 -> mapAreaId = buffer.readUnsignedShort()
            89 -> randomizeAnimStart = true
            90 -> delayAnimationUpdate = true
            249 -> readParameters(buffer)
            else -> logger.info { "Unable to decode Npcs [${opcode}]" }
        }
    }

}