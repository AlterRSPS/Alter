package dev.openrune.cache.filestore.definition.data

import dev.openrune.serialization.RscmString
import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.filestore.definition.Parameterized
import dev.openrune.cache.filestore.definition.Recolourable
import dev.openrune.cache.filestore.definition.Transforms
import dev.openrune.serialization.ListRscm
import dev.openrune.serialization.Rscm
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ObjectType(
    override var id: Rscm = -1,
    var name: String? = null,
    var decorDisplacement: Int = 16,
    var isHollow: Boolean = false,
    var objectModels: ListRscm? = null,
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
    var actions: MutableList<String?> = mutableListOf(null, null, null, null, null),
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
    var category: Int = -1,
    var supportsItems: Int = -1,
    var configs: IntArray? = null,
    var isRotated: Boolean = false,
    var varpId: Int = -1,
    var ambientSoundId: Int = -1,
    var modelClipped: Boolean = false,
    var soundMin: Int = 0,
    var soundMax: Int = 0,
    var delayAnimationUpdate: Boolean = false,
    var impenetrable: Boolean = true,
    override var originalColours: MutableList<Int>? = null,
    override var modifiedColours: MutableList<Int>? = null,
    override var originalTextureColours: MutableList<Int>? = null,
    override var modifiedTextureColours: MutableList<Int>? = null,
    override var varbit: Int = -1,
    override var varp: Int = -1,
    override var transforms: MutableList<Int>? = null,
    override var params: Map<Int, @Contextual Any>? = null,

    //Custom
    var option1: String? = null,
    var option2: String? = null,
    var option3: String? = null,
    var option4: String? = null,
    var option5: String? = null,
    @Serializable(RscmString::class) override var inherit: Int = -1

) : Definition, Transforms, Recolourable, Parameterized {
    init {
        actions = listOf(option1,option2,option3,option4,option5).toMutableList()
    }


    fun hasActions() = actions.any { it != null }

    override fun hashCode(): Int {
        return listOf(
            name.hashCode(),
            mapAreaId,
            actions.hashCode(),
            sizeX,
            sizeY,
            recolorToFind?.hashCode() ?: 0,
            retextureToReplace?.hashCode() ?: 0,
            objectModels?.hashCode() ?: 0,
            modelSizeX,
            modelSizeY,
            modelSizeZ,
            animationId
        ).fold(0) { acc, hash -> 31 * acc + hash }
    }

    // Optional: custom equals to match based on the same fields
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ObjectType) return false

        return name == other.name &&
                mapAreaId == other.mapAreaId &&
                actions == other.actions &&
                sizeX == other.sizeX &&
                sizeY == other.sizeY &&
                recolorToFind == other.recolorToFind &&
                retextureToReplace == other.retextureToReplace &&
                objectModels == other.objectModels &&
                modelSizeX == other.modelSizeX &&
                modelSizeY == other.modelSizeY &&
                modelSizeZ == other.modelSizeZ &&
                animationId == other.animationId
    }

}