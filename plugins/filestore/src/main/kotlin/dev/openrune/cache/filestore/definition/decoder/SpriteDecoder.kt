package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.CacheManager.cache
import dev.openrune.cache.PARAMS
import dev.openrune.cache.SPRITES
import dev.openrune.cache.filestore.Cache
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.ParamType
import dev.openrune.cache.filestore.definition.data.SpriteType
import dev.openrune.game.IndexedSprite
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

private const val FLAG_VERTICAL = 0x01
private const val FLAG_ALPHA = 0x02

class SpriteDecoder : DefinitionDecoder<SpriteType>(SPRITES) {

    override fun files() = cache.archives(SPRITES)

    override fun create(): Int2ObjectOpenHashMap<SpriteType> = createMap { SpriteType(it) }

    override fun getFile(id: Int) = 0

    override fun readLoop(definition: SpriteType, buffer: Reader) {
        definition.read(-1, buffer)
    }

    override fun SpriteType.read(opcode: Int, buffer: Reader) {
        buffer.position(buffer.array().size - 2)
        val size: Int = buffer.readShort()
        buffer.position(buffer.array().size - 7 - size * 8)

        val offsetX: Int = buffer.readShort()
        val offsetY: Int = buffer.readShort()

        val paletteSize: Int = buffer.readUnsignedByte() + 1

        val sprites = Array(size) { IndexedSprite() }
        for (index in 0 until size) {
            sprites[index].offsetX = buffer.readShort()
        }
        for (index in 0 until size) {
            sprites[index].offsetY = buffer.readShort()
        }
        for (index in 0 until size) {
            sprites[index].width = buffer.readShort()
        }
        for (index in 0 until size) {
            sprites[index].height = buffer.readShort()
        }
        for (index in 0 until size) {
            val sprite = sprites[index]
            sprite.deltaWidth = offsetX - sprite.width - sprite.offsetX
            sprite.deltaHeight = offsetY - sprite.height - sprite.offsetY
        }

        buffer.position(buffer.array().size - 7 - size * 8 - (paletteSize - 1) * 3)
        val palette = IntArray(paletteSize)
        for (index in 1 until paletteSize) {
            palette[index] = buffer.readUnsignedMedium()
            if (palette[index] == 0) {
                palette[index] = 1
            }
        }
        for (index in 0 until size) {
            sprites[index].palette = palette
        }

        buffer.position(0)
        for (index in 0 until size) {
            val sprite = sprites[index]
            val area = sprite.width * sprite.height

            sprite.raster = ByteArray(area)

            val setting: Int = buffer.readUnsignedByte()
            if (setting and 0x2 == 0) {
                if (setting and 0x1 == 0) {
                    for (pixel in 0 until area) {
                        sprite.raster[pixel] = buffer.readByte().toByte()
                    }
                } else {
                    for (x in 0 until sprite.width) {
                        for (y in 0 until sprite.height) {
                            sprite.raster[x + y * sprite.width] = buffer.readByte().toByte()
                        }
                    }
                }
            } else {
                var transparent = false
                val alpha = ByteArray(area)
                if (setting and 0x1 == 0) {
                    for (pixel in 0 until area) {
                        sprite.raster[pixel] = buffer.readByte().toByte()
                    }
                    for (pixel in 0 until area) {
                        alpha[pixel] = buffer.readByte().toByte()
                        val p = alpha[pixel].toInt()
                        transparent = transparent or (p != -1)
                    }
                } else {
                    for (x in 0 until sprite.width) {
                        for (y in 0 until sprite.height) {
                            sprite.raster[x + y * sprite.width] = buffer.readByte().toByte()
                        }
                    }
                    for (x in 0 until sprite.width) {
                        for (y in 0 until sprite.height) {
                            alpha[x + y * sprite.width] = buffer.readByte().toByte()
                            val pixel = alpha[x + y * sprite.width].toInt()
                            transparent = transparent or (pixel != -1)
                        }
                    }
                }
                if (transparent) {
                    sprite.alpha = alpha
                }
            }
        }
        this.sprites = sprites
    }

}