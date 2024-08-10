package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.CacheManager
import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.ObjectType

class ObjectEncoder : ConfigEncoder<ObjectType>() {

    override fun Writer.encode(definition: ObjectType) {
        if (definition.objectModels != null) {
            if (definition.objectTypes != null) {
                writeByte(1)
                writeByte(definition.objectModels!!.size)
                if (definition.objectModels!!.isNotEmpty()) {
                    for (i in 0 until definition.objectModels!!.size) {
                        writeShort(definition.objectModels!![i])
                        writeByte(definition.objectTypes!![i])
                    }
                }
            } else {
                writeByte(5)
                writeByte(definition.objectModels!!.size)
                if (definition.objectModels!!.isNotEmpty()) {
                    for (i in 0 until definition.objectModels!!.size) {
                        writeShort(definition.objectModels!![i])
                    }
                }
            }
        }

        if (definition.name != "null") {
            writeByte(2)
            writeString(definition.name)
        }


        writeByte(14)
        writeByte(definition.sizeX)

        writeByte(15)
        writeByte(definition.sizeY)


        if (definition.solid == 0 && !definition.impenetrable) {
            writeByte(17)
        }

        if (!definition.impenetrable) {
            writeByte(18)
        }

        if (definition.interactive != -1) {
            writeByte(19)
            writeByte(definition.interactive)
        }

        if (definition.clipType == 0) {
            writeByte(21)
        }

        if (definition.nonFlatShading) {
            writeByte(22)
        }

        if (definition.modelClipped) {
            writeByte(23)
        }

        if (definition.animationId != -1) {
            writeByte(24)
            writeShort(definition.animationId)
        }

        if (definition.solid == 1) {
            writeByte(27)
        }

        writeByte(28)
        writeByte(definition.decorDisplacement)

        writeByte(29)
        writeByte(definition.ambient)

        writeByte(39)
        writeByte(definition.contrast / 25)


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

        if (definition.category != -1) {
            writeByte(61)
            writeShort(definition.category)
        }

        if (definition.isRotated) {
            writeByte(62)
        }

        if (!definition.clipped) {
            writeByte(64)
        }

        writeByte(65)
        writeShort(definition.modelSizeX)

        writeByte(66)
        writeShort(definition.modelSizeZ)

        writeByte(67)
        writeShort(definition.modelSizeY)

        if (definition.mapSceneID != -1) {
            writeByte(68)
            writeShort(definition.mapSceneID)
        }

        if (definition.clipMask != 0) {
            writeByte(69)
            writeByte(definition.clipMask)
        }

        writeByte(70)
        writeShort(definition.offsetX)

        writeByte(71)
        writeShort(definition.offsetZ)

        writeByte(72)
        writeShort(definition.offsetY)

        if (definition.obstructive) {
            writeByte(73)
        }

        if (definition.isHollow) {
            writeByte(74)
        }

        if (definition.supportsItems != -1) {
            writeByte(75)
            writeByte(definition.supportsItems)
        }

        if (definition.ambientSoundId != -1) {
            writeByte(78)
            writeShort(definition.ambientSoundId)
            writeByte(definition.soundDistance)
            if (CacheManager.revisionIsOrAfter(220)) {
                writeByte(definition.soundRetain)
            }
        }

        if (definition.ambientSoundIds != null) {
            writeByte(79)
            writeShort(definition.soundMin)
            writeShort(definition.soundMax)
            writeByte(definition.soundDistance)
            if (CacheManager.revisionIsOrAfter(220)) {
                writeByte(definition.soundRetain)
            }

            writeByte(definition.ambientSoundIds!!.size)
            for (i in definition.ambientSoundIds!!.indices) {
                writeShort(definition.ambientSoundIds!![i])
            }
        }

        if (definition.clipType != -1) {
            writeByte(81)
            writeByte(definition.clipType / 256)
        }

        if (definition.mapAreaId != -1) {
            writeByte(82)
            writeShort(definition.mapAreaId)
        }

        if (!definition.randomizeAnimStart) {
            writeByte(89)
        }

        if (definition.delayAnimationUpdate) {
            writeByte(90)
        }

        definition.writeTransforms(this, 77,92)
        definition.writeParameters(this)

        writeByte(0)
    }

}