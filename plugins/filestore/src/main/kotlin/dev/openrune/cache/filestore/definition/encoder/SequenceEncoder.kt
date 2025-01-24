package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.CacheManager
import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.SequenceType

class SequenceEncoder: ConfigEncoder<SequenceType>() {

    override fun Writer.encode(definition: SequenceType) {

        if (definition.frameIDs != null) {
            writeByte(1)
            writeShort(definition.frameIDs!!.size)
            for (i in 0 until definition.frameDelays!!.size) {
                writeShort(definition.frameDelays!![i])
            }
            for (i in 0 until definition.frameIDs!!.size) {
                writeShort(definition.frameIDs!![i])
            }
            for (i in 0 until definition.frameIDs!!.size) {
                writeShort(definition.frameIDs!![i] shr 16)
            }
        }

        if (definition.frameStep != -1) {
            writeByte(2)
            writeShort(definition.frameStep)
        }

        if (definition.interleaveLeave != null) {
            writeByte(3)
            writeByte(definition.interleaveLeave!!.size)
            for (i in 0 until definition.interleaveLeave!!.size) {
                writeByte(definition.interleaveLeave!![i])
            }
        }

        if (definition.stretches) {
            writeByte(4)
        }

        if (definition.forcedPriority != 5) {
            writeByte(5)
            writeByte(definition.forcedPriority)
        }

        if (definition.leftHandItem != -1) {
            writeByte(6)
            writeShort(definition.leftHandItem)
        }

        if (definition.rightHandItem != -1) {
            writeByte(7)
            writeShort(definition.rightHandItem)
        }

        if (definition.maxLoops != 99) {
            writeByte(8)
            writeByte(definition.maxLoops)
        }

        if (definition.precedenceAnimating != -1) {
            writeByte(9)
            writeByte(definition.precedenceAnimating)
        }

        if (definition.priority != -1) {
            writeByte(10)
            writeByte(definition.priority)
        }

        if (definition.replyMode != 2) {
            writeByte(11)
            writeByte(definition.replyMode)
        }

        if (definition.chatFrameIds != null) {
            writeByte(12)
            writeByte(definition.chatFrameIds!!.size)

            for (i in 0 until definition.chatFrameIds!!.size) {
                writeShort(definition.chatFrameIds!![i])
            }
            for (i in 0 until definition.chatFrameIds!!.size) {
                writeShort(definition.chatFrameIds!![i] shr 16)
            }

        }

        if (CacheManager.cacheRevision >= 256) {
            if (definition.skeletalId != -1) {
                writeByte(13)
                writeInt(definition.skeletalId)
            }
        } else {
            if (definition.soundEffects.isNotEmpty()) {
                writeByte(13)
                writeByte(definition.soundEffects.size)
                definition.soundEffects.forEach {
                    it!!.writeSound(this, CacheManager.revisionIsOrAfter(220))
                }
            }
        }

        if (CacheManager.cacheRevision >= 256) {
            if (definition.sounds.isNotEmpty()) {
                writeByte(14)
                writeShort(definition.sounds.size)
                definition.sounds.forEach { (index, sound) ->
                    writeShort(index)
                    sound.writeSound(this, CacheManager.revisionIsOrAfter(220))
                }
            }
        } else {
            if (definition.skeletalId != -1) {
                writeByte(14)
                writeInt(definition.skeletalId)
            }
        }

        if (CacheManager.cacheRevision >= 256) {
            if (definition.rangeBegin != 0 || definition.rangeEnd != 0) {
                writeByte(15)
                writeShort(definition.rangeBegin)
                writeShort(definition.rangeEnd)
            }
        } else {
            if (definition.sounds.isNotEmpty()) {
                writeByte(15)
                writeShort(definition.sounds.size)
                definition.sounds.forEach { (index, sound) ->
                    writeShort(index)
                    sound.writeSound(this, CacheManager.revisionIsOrAfter(220))
                }
            }
        }

        if (CacheManager.cacheRevision < 256) {
            if (definition.rangeBegin != 0 || definition.rangeEnd != 0) {
                writeByte(16)
                writeShort(definition.rangeBegin)
                writeShort(definition.rangeEnd)
            }
        }

        if (definition.mask != null) {
            writeByte(17)

            writeByte(definition.mask!!.filter { it }.size)
            definition.mask!!.forEachIndexed { index, state ->
                if (state) {
                    writeByte(index)
                }
            }

        }

        writeByte(0)
    }


}