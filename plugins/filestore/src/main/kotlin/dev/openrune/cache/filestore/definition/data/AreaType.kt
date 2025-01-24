package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition

data class AreaType(
    override var id: Int = -1,
    var sprite1: Int = -1,

    var sprite2: Int = -1,

    var name: String = "null",

    var fontColor: Int = 0,

    var textSize: Int = 0,

    var renderOnWorldMap: Boolean = true,

    var renderOnMinimap: Boolean = false,

    var options : MutableList<String?> = mutableListOf(null, null, null, null, null),

    var menuTargetName: String = "null",

    var field1933: MutableList<Int>? = null,

    var horizontalAlignment: Int = 1,

    var verticalAlignment: Int = 1,

    var field1930: MutableList<Int> = emptyList<Int>().toMutableList(),

    var field1948: MutableList<Int> = emptyList<Int>().toMutableList(),

    var category: Int = 0,
    //Custom
    override var inherit: Int = -1,
    var option1: String? = null,
    var option2: String? = null,
    var option3: String? = null,
    var option4: String? = null,
    var option5: String? = null,
) : Definition {
    init {
        options = listOf(option1,option2,option3,option4,option5).toMutableList()
    }
}