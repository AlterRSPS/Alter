package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.CONFIGS
import dev.openrune.cache.CacheManager
import dev.openrune.cache.SEQUENCE
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.definition.data.SequenceType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import kotlin.math.ceil


class SequenceDecoder : DefinitionDecoder<SequenceType>(CONFIGS) {

    override fun getArchive(id: Int) = SEQUENCE

    override fun create(): Int2ObjectOpenHashMap<SequenceType> = createMap { SequenceType(it) }

    override fun getFile(id: Int) = id

    override fun SequenceType.read(opcode: Int, buffer: Reader) {
        when (opcode) {
            1 -> {
                val frameCount = buffer.readUnsignedShort()
                var totalFrameLength = 0
                frameIDs = MutableList(frameCount) { 0 }
                frameDelays = MutableList(frameCount) { 0 }

                for (i in 0 until frameCount) {
                    frameDelays!![i] = buffer.readUnsignedShort()
                    if (i < frameCount - 1 || frameDelays!![i] < 200) {
                        totalFrameLength += frameDelays!![i]
                    }
                }

                for (i in 0 until frameCount) {
                    frameIDs!![i] = buffer.readUnsignedShort()
                }

                for (i in 0 until frameCount) {
                    frameIDs!![i] += buffer.readUnsignedShort() shl 16
                }

                lengthInCycles = ceil((totalFrameLength * 20.0) / 600.0).toInt()
            }

            2 -> frameStep = buffer.readUnsignedShort()
            3 -> {
                val count = buffer.readUnsignedByte()
                interleaveLeave = MutableList(count + 1) { 0 }
                for (i in 0 until count) {
                    interleaveLeave!![i] = buffer.readUnsignedByte()
                }
                interleaveLeave!![count] = 0x98967f
            }

            4 -> stretches = true
            5 -> forcedPriority = buffer.readUnsignedByte()
            6 -> leftHandItem = buffer.readUnsignedShort()
            7 -> rightHandItem = buffer.readUnsignedShort()
            8 -> maxLoops = buffer.readUnsignedByte()
            9 -> precedenceAnimating = buffer.readUnsignedByte()
            10 -> priority = buffer.readUnsignedByte()
            11 -> replyMode = buffer.readUnsignedByte()
            12 -> {
                val count = buffer.readUnsignedByte()
                chatFrameIds = MutableList(count) { 0 }
                for (i in 0 until count) {
                    chatFrameIds!![i] = buffer.readUnsignedShort()
                }

                for (i in 0 until count) {
                    chatFrameIds!![i] += buffer.readUnsignedShort() shl 16
                }
            }

            13 -> {
                if (CacheManager.cacheRevision >= 226) {
                    skeletalId = buffer.readInt()
                } else {
                    val count = buffer.readUnsignedByte()
                    for (i in 0 until count) {
                        sounds[i] = readSounds(buffer, CacheManager.revisionIsOrAfter(220))!!
                    }
                }
            }

            14 -> {
                if (CacheManager.cacheRevision >= 226) {
                    val count = buffer.readUnsignedShort()
                    for (i in 0 until count) {
                        val index = buffer.readUnsignedShort()
                        val sound = readSounds(buffer, CacheManager.revisionIsOrAfter(220))
                        sounds[index] = sound!!
                    }
                } else {
                    skeletalId = buffer.readInt()
                }
            }
            15 -> {
                if (CacheManager.cacheRevision >= 226) {
                    rangeBegin = buffer.readUnsignedShort()
                    rangeEnd = buffer.readUnsignedShort()
                } else {
                    val count = buffer.readUnsignedShort()
                    for (i in 0 until count) {
                        val index = buffer.readUnsignedShort()
                        val sound = readSounds(buffer, CacheManager.revisionIsOrAfter(220))
                        sounds[index] = sound!!
                    }
                }
            }

            16 -> {
                if (CacheManager.cacheRevision < 226) {
                    rangeBegin = buffer.readUnsignedShort()
                    rangeEnd = buffer.readUnsignedShort()
                }
            }

            17 -> {
                mask = MutableList(256) { false }
                val count = buffer.readUnsignedByte()
                for (i in 0 until count) {
                    mask!![buffer.readUnsignedByte()] = true
                }
            }
        }
    }
}