package org.alter.game.fs

import java.io.IOException
import java.nio.ByteBuffer

class RLTile {
    var height: Int? = null
    var attrOpcode = 0
    var settings: Byte = 0
    var overlayId: Short = 0
    var overlayPath: Byte = 0
    var overlayRotation: Byte = 0
    var underlayId: Short = 0
}

class RLPosition(val x: Int, val y: Int, val z: Int)

class RLLocation(val id: Int, val type: Int, val orientation: Int, val position: RLPosition)

fun loadLocations(b: ByteArray): List<RLLocation> {
    val locations: MutableList<RLLocation> = ArrayList()
    val buf = InputStream(b)
    var id = -1
    var idOffset: Int
    while (buf.readUnsignedIntSmartShortCompat().also { idOffset = it } != 0) {
        id += idOffset
        var position = 0
        var positionOffset: Int
        while (buf.readUnsignedShortSmart().also { positionOffset = it } != 0) {
            position += positionOffset - 1
            val localY = position and 63
            val localX = position shr 6 and 63
            val height = position shr 12 and 3
            val attributes = buf.readUnsignedByte()
            val type = attributes shr 2
            val orientation = attributes and 3
            locations.add(RLLocation(id, type, orientation, RLPosition(localX, localY, height)))
        }
    }
    return locations
}

fun loadTerrain(b: ByteArray): Array<Array<Array<RLTile?>>> {
    val tiles = Array(4) { Array(64) { arrayOfNulls<RLTile>(64) } }
    val buf = InputStream(b)
    for (z in 0..3) {
        for (x in 0..63) {
            for (y in 0..63) {
                val tile = RLTile()
                tiles[z][x][y] = tile

                while (true) {
                    val attribute = buf.readUnsignedShort()
                    if (attribute == 0) {
                        break
                    }
                    if (attribute == 1) {
                        val height = buf.readUnsignedByte()
                        tile.height = height
                        break
                    }
                    if (attribute <= 49) {
                        tile.attrOpcode = attribute
                        tile.overlayId = buf.readShort()
                        tile.overlayPath = ((attribute - 2) / 4).toByte()
                        tile.overlayRotation = (attribute - 2 and 3).toByte()
                    } else if (attribute <= 81) {
                        tile.settings = (attribute - 49).toByte()
                    } else {
                        tile.underlayId = (attribute - 81).toShort()
                    }
                }
            }
        }
    }
    return tiles
}

class InputStream(buffer: ByteArray?) : java.io.InputStream() {
    private val buffer: ByteBuffer

    init {
        this.buffer = ByteBuffer.wrap(buffer)
    }

    override fun toString(): String {
        return "InputStream{buffer=" + buffer + "}"
    }

    fun readByte(): Byte {
        return buffer.get()
    }

    fun readUnsignedByte(): Int {
        return readByte().toInt() and 255
    }

    fun readUnsignedShort(): Int {
        return buffer.getShort().toInt() and '\uffff'.code
    }

    fun readShort(): Short {
        return buffer.getShort()
    }

    fun peek(): Byte {
        return buffer[buffer.position()]
    }

    fun readUnsignedShortSmart(): Int {
        val peek = peek().toInt() and 255
        return if (peek < 128) readUnsignedByte() else readUnsignedShort() - '耀'.code
    }

    fun readUnsignedIntSmartShortCompat(): Int {
        var var1 = 0
        var var2: Int
        var2 = readUnsignedShortSmart()
        while (var2 == 32767) {
            var1 += 32767
            var2 = readUnsignedShortSmart()
        }
        var1 += var2
        return var1
    }

    @Throws(IOException::class)
    override fun read(): Int {
        return readUnsignedByte()
    }
}

