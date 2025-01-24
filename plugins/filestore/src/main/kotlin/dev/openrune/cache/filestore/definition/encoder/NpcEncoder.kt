package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.CacheManager
import dev.openrune.cache.CacheManager.revisionIsOrAfter
import dev.openrune.cache.filestore.buffer.BufferWriter
import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.NpcType

class NpcEncoder : ConfigEncoder<NpcType>() {

    override fun Writer.encode(definition: NpcType) {
        if (definition.models != null && definition.models!!.isNotEmpty()) {
            writeByte(1)
            writeByte(definition.models!!.size)
            for (i in definition.models!!.indices) {
                writeShort(definition.models!![i])
            }
        }

        if (definition.name != "null") {
            writeByte(2)
            writeString(definition.name)
        }

        if (definition.size != -1) {
            writeByte(12)
            writeByte(definition.size)
        }

        if (definition.standAnim != -1) {
            writeByte(13)
            writeShort(definition.standAnim)
        }

        if (definition.walkAnim != -1) {
            writeByte(14)
            writeShort(definition.walkAnim)
        }

        if (definition.rotateLeftAnim != -1) {
            writeByte(15)
            writeShort(definition.rotateLeftAnim)
        }

        if (definition.rotateRightAnim != -1) {
            writeByte(16)
            writeShort(definition.rotateRightAnim)
        }

        if (definition.walkAnim != -1 || definition.rotateBackAnim != -1 || definition.walkLeftAnim != -1 || definition.walkRightAnim != -1) {
            writeByte(17)
            writeShort(definition.walkAnim)
            writeShort(definition.rotateBackAnim)
            writeShort(definition.walkLeftAnim)
            writeShort(definition.walkRightAnim)
        }

        if (definition.category != -1) {
            writeByte(18)
            writeShort(definition.category)
        }

        if (definition.actions.any { it != null }) {
            for (i in 0 until definition.actions.size) {
                if (definition.actions[i] == null) {
                    continue
                }
                writeByte(30 + i)
                writeString(definition.actions[i]!!)
            }
        }

        definition.writeColoursTextures(this)

        if (definition.chatheadModels != null) {
            writeByte(60)
            writeByte(definition.chatheadModels!!.size)
            for (i in definition.chatheadModels!!.indices) {
                writeShort(definition.chatheadModels!![i])
            }
        }

        for (i in 0 .. 5) {
            if (definition.stats[i] != 1) {
                writeByte(74 + i)
                writeShort(definition.stats[i])
            }
        }

        if (!definition.isMinimapVisible) {
            writeByte(93)
        }
        if (definition.combatLevel != -1) {
            writeByte(95)
            writeShort(definition.combatLevel)
        }


        writeByte(97)
        writeShort(definition.widthScale)

        writeByte(98)
        writeShort(definition.heightScale)


        if (definition.hasRenderPriority) {
            writeByte(99)
        }


        writeByte(100)
        writeByte(definition.ambient)

        writeByte(101)
        writeByte(definition.contrast)

        if (definition.headIconSpriteIndex != null) {
            writeByte(102)
            if (CacheManager.revisionIsOrBefore(210)) {
                writeShort(definition.headIconSpriteIndex!!.first())
            } else {
                writeShort(definition.headIconArchiveIds!!.size)
                repeat(definition.headIconArchiveIds!!.size) {
                    writeShort(definition.headIconArchiveIds!![it])
                    writeShort(definition.headIconSpriteIndex!![it])
                }
            }
        }

        writeByte(103)
        writeShort(definition.rotation)

        definition.writeTransforms(this, 106, 118)

        if (!definition.isInteractable) {
            writeByte(107)
        }

        if (!definition.isClickable) {
            writeByte(109)
        }

        if (revisionIsOrAfter(220)) {
            if (definition.lowPriorityFollowerOps) {
                writeByte(122)
            }
            if (definition.isFollower) {
                writeByte(123)
            }
        } else {
            if (definition.isFollower) {
                writeByte(111)
            }
        }

        if (definition.runSequence != -1) {
            writeByte(114)
            writeShort(definition.runSequence)
        }

        if (definition.runSequence != -1) {
            writeByte(115)
            writeShort(definition.runSequence)
            writeShort(definition.runBackSequence)
            writeShort(definition.runRightSequence)
            writeShort(definition.runLeftSequence)
        }

        if (definition.crawlSequence != -1) {
            writeByte(116)
            writeShort(definition.crawlSequence)
        }

        if (definition.crawlSequence != -1) {
            writeByte(117)
            writeShort(definition.crawlSequence)
            writeShort(definition.crawlBackSequence)
            writeShort(definition.crawlRightSequence)
            writeShort(definition.crawlLeftSequence)
        }

        if(definition.height != -1) {
            writeByte(124)
            writeShort(definition.height)
        }

        definition.writeParameters(this)

        writeByte(0)
    }

}