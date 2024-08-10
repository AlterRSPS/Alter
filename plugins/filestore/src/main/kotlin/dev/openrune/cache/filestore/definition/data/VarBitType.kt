package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition

data class VarBitType(
    override var id: Int = -1,
    var varp: Int = 0,
    var startBit: Int = 0,
    var endBit: Int = 0,
    //Custom
    override var inherit: Int = -1
) : Definition