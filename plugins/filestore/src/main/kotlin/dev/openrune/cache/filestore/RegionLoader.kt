package dev.openrune.cache.filestore

import dev.openrune.cache.CacheManager
import dev.openrune.cache.filestore.buffer.BufferReader

class TileData {
    var height = 0
    var attrOpcode = 0
    var settings: Byte = 0
    var overlayId: Short = 0
    var overlayPath: Byte = 0
    var overlayRotation: Byte = 0
    var underlayId: Short = 0
}

data class LocationData(val id: Int, val type: Int, val orientation: Int, val localX: Int, val localY: Int, val height: Int)

fun loadLocations(b: ByteArray, fn: (LocationData) -> Unit) {
    val buf = BufferReader(b)
    var id = -1
    var idOffset: Int
    while (buf.readLargeSmart().also { idOffset = it } != 0) {
        id += idOffset
        var position = 0
        var positionOffset: Int
        while (buf.readSmart().also { positionOffset = it } != 0) {
            position += positionOffset - 1
            val localY = position and 63
            val localX = position shr 6 and 63
            val height = position shr 12 and 3
            val attributes = buf.readUnsignedByte()
            val type = attributes shr 2
            val orientation = attributes and 3
            fn.invoke(LocationData(id, type, orientation, localX, localY, height))
        }
    }
}

fun loadTerrain(b: ByteArray, after208: Boolean = CacheManager.revisionIsOrAfter(209)): Array<Array<Array<TileData>>> {
    val tiles = Array(4) { Array(64) { Array(64) { TileData() } } }
    val buf = BufferReader(b)
    for (z in 0 until 4) {
        for (x in 0 until 64) {
            for (y in 0 until 64) {
                val tile = tiles[z][x][y]

                while (true) {
                    val attribute = if (after208) buf.readUnsignedShort() else buf.readUnsignedByte()
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
                        tile.overlayId = if (after208) buf.readShort().toShort() else buf.readByte().toShort()
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