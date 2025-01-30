package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition


data class TextureType(
    override var id : Int = -1,
    var isTransparent : Boolean = false,
    var fileIds : MutableList<Int> = emptyList<Int>().toMutableList(),
    var combineModes : MutableList<Int> = emptyList<Int>().toMutableList(),
    var field2440 : MutableList<Int> = emptyList<Int>().toMutableList(),
    var colourAdjustments : MutableList<Int> = emptyList<Int>().toMutableList(),
    var averageRgb : Int = 0,
    var animationDirection : Int = 0,
    var animationSpeed : Int = 0,
    override var inherit : Int = -1,
 ) : Definition {

    @Transient
    lateinit var pixels: IntArray

 }