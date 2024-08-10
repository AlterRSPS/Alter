package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.CacheManager
import dev.openrune.cache.NPC
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.definition.data.NpcType


class NPCDecoder : DefinitionDecoder<NpcType>(NPC) {
    override fun create(size: Int) = Array(size) { NpcType(it) }

    override fun getFile(id: Int) = id

    override fun NpcType.read(opcode: Int, buffer: Reader) {
        when (opcode) {
            1 -> {
                val length = buffer.readUnsignedByte()
                models = MutableList(length) { 0 }
                for (count in 0 until length) {
                    models!![count] = buffer.readUnsignedShort()
                    if (models!![count] == 65535) {
                        models!![count] = -1
                    }
                }
            }
            2 -> name = buffer.readString()
            12 -> size = buffer.readUnsignedByte()
            13 -> standAnim = buffer.readUnsignedShort()
            14 -> walkAnim = buffer.readUnsignedShort()
            15 -> rotateLeftAnim = buffer.readUnsignedShort()
            16 -> rotateRightAnim = buffer.readUnsignedShort()
            17 -> {
                walkAnim = buffer.readUnsignedShort()
                rotateBackAnim = buffer.readUnsignedShort()
                walkLeftAnim = buffer.readUnsignedShort()
                walkRightAnim = buffer.readUnsignedShort()
            }
            18 -> category = buffer.readUnsignedShort()
            in 30..34 -> {
                actions[opcode - 30] = buffer.readString()
                if (actions[opcode - 30].equals("Hidden", true)) {
                    actions[opcode - 30] = null
                }
            }
            40 -> readColours(buffer)
            41 -> readTextures(buffer)
            60 -> {
                val length: Int = buffer.readUnsignedByte()
                chatheadModels = MutableList(length) { 0 }
                (0 until length).forEach {
                    chatheadModels!![it] = buffer.readUnsignedShort()
                }
            }
            in 74..79 -> stats[opcode - 74] = buffer.readUnsignedShort()
            93 -> isMinimapVisible = false
            95 -> combatLevel = buffer.readUnsignedShort()
            97 -> widthScale = buffer.readUnsignedShort()
            98 -> heightScale = buffer.readUnsignedShort()
            99 -> hasRenderPriority = true
            100 -> ambient = buffer.readByte()
            101 -> contrast = buffer.readByte()
            102 -> {
                if (CacheManager.revisionIsOrBefore(210)) {
                    headIconArchiveIds = MutableList(0) { 0 }
                    headIconSpriteIndex = MutableList(buffer.readUnsignedShort()) { 0 }
                } else {
                    val bitfield = buffer.readUnsignedByte()
                    var size = 0

                    var pos = bitfield
                    while (pos != 0) {
                        ++size
                        pos = pos shr 1
                    }
                    headIconArchiveIds =  MutableList(size) { 0 }
                    headIconSpriteIndex = MutableList(size) { 0 }

                    for (i in 0 until size) {
                        if (bitfield and (1 shl i) == 0) {
                            headIconArchiveIds!![i] = -1
                            headIconSpriteIndex!![i] = -1
                        } else {
                            headIconArchiveIds!![i] = buffer.readUnsignedShort()
                            headIconSpriteIndex!![i] = buffer.readShortSmart() - 1
                        }
                    }
                }
            }
            111 -> isFollower = true
            103 -> rotation = buffer.readUnsignedShort()
            106, 118 -> readTransforms(buffer, opcode == 118)
            107 -> isInteractable = false
            109 -> isClickable = false
            114 -> runSequence = buffer.readUnsignedShort()
            115 -> {
                runSequence = buffer.readUnsignedShort()
                runBackSequence = buffer.readUnsignedShort()
                runRightSequence = buffer.readUnsignedShort()
                runLeftSequence = buffer.readUnsignedShort()
            }
            116 -> crawlSequence = buffer.readUnsignedShort()
            117 -> {
                crawlSequence = buffer.readUnsignedShort()
                crawlBackSequence = buffer.readUnsignedShort()
                crawlRightSequence = buffer.readUnsignedShort()
                crawlLeftSequence = buffer.readUnsignedShort()
            }
            122 -> lowPriorityFollowerOps = true
            123 -> isFollower = true
            124 -> height = buffer.readUnsignedShort()
            249 -> readParameters(buffer)
            else -> logger.info { "Unable to decode Npcs [${opcode}]" }
        }
    }

}