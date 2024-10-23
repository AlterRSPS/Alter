package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition

data class HealthBarType(
    override var id: Int = -1,
    var int1: Int = 255,
    var int2: Int = 255,
    var int3: Int = -1,
    var int4: Int = 70,
    var frontSpriteId: Int = -1,
    var backSpriteId: Int = -1,
    var width: Int = 30,
    var widthPadding: Int = 0,
    //Custom
    override var inherit: Int = -1
) : Definition