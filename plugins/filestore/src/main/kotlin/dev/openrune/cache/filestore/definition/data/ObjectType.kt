package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.filestore.definition.Parameterized
import dev.openrune.cache.filestore.definition.Recolourable
import dev.openrune.cache.filestore.definition.Transforms

data class ObjectType(
    override var id: Int = -1,
    var name: String = "null",
    var decorDisplacement : Int = 16,
    var isHollow : Boolean = false,
    var objectModels: MutableList<Int>? = null,
    var objectTypes: MutableList<Int>? = null,
    var recolorToFind: MutableList<Int>? = null,
    var mapAreaId: Int = -1,
    var retextureToReplace: MutableList<Int>? = null,
    var sizeX: Int = 1,
    var sizeY: Int = 1,
    var soundDistance: Int = 0,
    var soundRetain: Int = 0,
    var ambientSoundIds: MutableList<Int>? = null,
    var offsetX: Int = 0,
    var nonFlatShading: Boolean = false,
    var interactive: Int = -1,
    var animationId: Int = -1,
    var varbitId: Int = -1,
    var ambient: Int = 0,
    var contrast: Int = 0,
    var actions : MutableList<String?> = mutableListOf(null, null, null, null, null),
    var solid: Int = 2,
    var mapSceneID: Int = -1,
    var clipMask: Int = 0,
    var recolorToReplace: List<Short>? = null,
    var clipped: Boolean = true,
    var modelSizeX: Int = 128,
    var modelSizeZ: Int = 128,
    var modelSizeY: Int = 128,
    var offsetZ: Int = 0,
    var offsetY: Int = 0,
    var obstructive: Boolean = false,
    var randomizeAnimStart: Boolean = true,
    var clipType: Int = -1,
    var category : Int = -1,
    var supportsItems: Int = -1,
    var configs: IntArray? = null,
    var isRotated: Boolean = false,
    var varpId: Int = -1,
    var ambientSoundId: Int = -1,
    var modelClipped: Boolean = false,
    var soundMin: Int = 0,
    var soundMax: Int = 0,
    var delayAnimationUpdate : Boolean = false,
    var impenetrable: Boolean = true,
    override var originalColours: MutableList<Int>? = null,
    override var modifiedColours: MutableList<Int>? = null,
    override var originalTextureColours: MutableList<Int>? = null,
    override var modifiedTextureColours: MutableList<Int>? = null,
    override var varbit: Int = -1,
    override var varp: Int = -1,
    override var transforms: MutableList<Int>? = null,
    override var params: Map<Int, Any>? = null,

    //Custom
    var option1: String? = null,
    var option2: String? = null,
    var option3: String? = null,
    var option4: String? = null,
    var option5: String? = null,
    override var inherit : Int = -1

) : Definition, Transforms, Recolourable, Parameterized {
    init {
        actions = listOf(option1,option2,option3,option4,option5).toMutableList()
    }
}