package dev.openrune.cache.util.secure

class VersionTableBuilder(
    indexCount: Int
) {

    private val crc = CRC()
    private val versionTable = ByteArray(positionFor(indexCount))

    init {
        val size = indexCount * 8
        versionTable[0] = 0
        versionTable[1] = (size shr 24).toByte()
        versionTable[2] = (size shr 16).toByte()
        versionTable[3] = (size shr 8).toByte()
        versionTable[4] = (size).toByte()
    }

    fun skip(index: Int) {
        // TODO should not be needed because array should be filled with zeros.
    }

    fun sector(index: Int, sectorData: ByteArray) {
        val crc = crc.calculate(sectorData)
        crc(index, crc)
    }

    fun crc(index: Int, crc: Int) {
        val pos = positionFor(index)
        versionTable[pos] = (crc shr 24).toByte()
        versionTable[pos + 1] = (crc shr 16).toByte()
        versionTable[pos + 2] = (crc shr 8).toByte()
        versionTable[pos + 3] = (crc).toByte()
    }

    fun revision(index: Int, revision: Int) {
        val pos = positionFor(index) + 4
        versionTable[pos] = (revision shr 24).toByte()
        versionTable[pos + 1] = (revision shr 16).toByte()
        versionTable[pos + 2] = (revision shr 8).toByte()
        versionTable[pos + 3] = (revision).toByte()
    }

    fun build(): ByteArray {
        return versionTable
    }

    companion object {
        private fun positionFor(index: Int) = 5 + index * 8
    }
}