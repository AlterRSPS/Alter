package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.filestore.definition.Transforms

data class HitSplatType(
    override var id: Int = -1,
    var font: Int = -1,
    var textColour: Int = 16777215,
    var icon: Int = -1,
    var left: Int = -1,
    var middle: Int = -1,
    var right: Int = -1,
    var offsetX: Int = 0,
    var amount: String = "",
    var duration: Int = 70,
    var offsetY: Int = 0,
    var fade: Int = -1,
    var comparisonType: Int = -1,
    var damageYOfset: Int = 0,
    override var varbit: Int = -1,
    override var varp: Int = -1,
    override var transforms: MutableList<Int>? = null,
    //Custom
    override var inherit: Int = -1
) : Definition, Transforms