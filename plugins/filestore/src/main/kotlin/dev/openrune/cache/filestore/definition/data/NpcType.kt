package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.filestore.definition.Parameterized
import dev.openrune.cache.filestore.definition.Recolourable
import dev.openrune.cache.filestore.definition.Transforms
import dev.openrune.serialization.ListRscm
import dev.openrune.serialization.Rscm
import dev.openrune.serialization.RscmList
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class NpcType(
    override var id: Rscm = -1,
    var name: String = "null",
    var size : Int = 1,
    var category : Int = -1,
    var models: ListRscm? = null,
    var chatheadModels: ListRscm? = null,
    var standAnim : Int = -1,
    var rotateLeftAnim : Int = -1,
    var rotateRightAnim : Int = -1,
    var walkAnim : Int = -1,
    var rotateBackAnim : Int = -1,
    var walkLeftAnim : Int = -1,
    var walkRightAnim : Int = -1,
    var actions : MutableList<String?> = mutableListOf(null, null, null, null, null),
    override var originalColours: MutableList<Int>? = null,
    override var modifiedColours: MutableList<Int>? = null,
    override var originalTextureColours: MutableList<Int>? = null,
    override var modifiedTextureColours: MutableList<Int>? = null,
    override var varbit: Int = -1,
    override var varp: Int = -1,
    override var transforms: MutableList<Int>? = null,
    var isMinimapVisible : Boolean = true,
    var combatLevel : Int = -1,
    var widthScale : Int = 128,
    var heightScale : Int = 128,
    var hasRenderPriority : Boolean = false,
    var ambient : Int = 0,
    var contrast : Int = 0,
    var headIconArchiveIds: MutableList<Int>? = null,
    var headIconSpriteIndex: MutableList<Int>? = null,
    var rotation : Int = 32,
    var isInteractable : Boolean = true,
    var isClickable : Boolean = true,
    var lowPriorityFollowerOps : Boolean = false,
    var isFollower : Boolean = false,
    var runSequence : Int = -1,
    var runBackSequence : Int = -1,
    var runRightSequence : Int = -1,
    var runLeftSequence : Int = -1,
    var crawlSequence : Int = -1,
    var crawlBackSequence : Int = -1,
    var crawlRightSequence : Int = -1,
    var crawlLeftSequence : Int = -1,
    override var params: Map<Int, @Contextual Any>? = null,
    var height: Int = -1,
    var stats: IntArray = intArrayOf(1, 1, 1, 1, 1, 1),

    //Custom
    override var inherit: Int = -1,
    var option1: String? = null,
    var option2: String? = null,
    var option3: String? = null,
    var option4: String? = null,
    var option5: String? = null,

    ) : Definition, Transforms, Recolourable, Parameterized {

    init {
        actions = listOf(option1,option2,option3,option4,option5).toMutableList()
    }

    var examine : String = ""

    fun isAttackable(): Boolean = combatLevel > 0 && actions.any { it == "Attack" }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NpcType

        if (id != other.id) return false
        if (name != other.name) return false
        if (size != other.size) return false
        if (category != other.category) return false
        if (models != other.models) return false
        if (chatheadModels != other.chatheadModels) return false
        if (standAnim != other.standAnim) return false
        if (rotateLeftAnim != other.rotateLeftAnim) return false
        if (rotateRightAnim != other.rotateRightAnim) return false
        if (walkAnim != other.walkAnim) return false
        if (rotateBackAnim != other.rotateBackAnim) return false
        if (walkLeftAnim != other.walkLeftAnim) return false
        if (walkRightAnim != other.walkRightAnim) return false
        if (actions != other.actions) return false
        if (originalColours != other.originalColours) return false
        if (modifiedColours != other.modifiedColours) return false
        if (originalTextureColours != other.originalTextureColours) return false
        if (modifiedTextureColours != other.modifiedTextureColours) return false
        if (varbit != other.varbit) return false
        if (varp != other.varp) return false
        if (transforms != other.transforms) return false
        if (isMinimapVisible != other.isMinimapVisible) return false
        if (combatLevel != other.combatLevel) return false
        if (widthScale != other.widthScale) return false
        if (heightScale != other.heightScale) return false
        if (hasRenderPriority != other.hasRenderPriority) return false
        if (ambient != other.ambient) return false
        if (contrast != other.contrast) return false
        if (headIconArchiveIds != other.headIconArchiveIds) return false
        if (headIconSpriteIndex != other.headIconSpriteIndex) return false
        if (rotation != other.rotation) return false
        if (isInteractable != other.isInteractable) return false
        if (isClickable != other.isClickable) return false
        if (lowPriorityFollowerOps != other.lowPriorityFollowerOps) return false
        if (isFollower != other.isFollower) return false
        if (runSequence != other.runSequence) return false
        if (runBackSequence != other.runBackSequence) return false
        if (runRightSequence != other.runRightSequence) return false
        if (runLeftSequence != other.runLeftSequence) return false
        if (crawlSequence != other.crawlSequence) return false
        if (crawlBackSequence != other.crawlBackSequence) return false
        if (crawlRightSequence != other.crawlRightSequence) return false
        if (crawlLeftSequence != other.crawlLeftSequence) return false
        if (params != other.params) return false
        if (inherit != other.inherit) return false
        if (option1 != other.option1) return false
        if (option2 != other.option2) return false
        if (option3 != other.option3) return false
        if (option4 != other.option4) return false
        if (option5 != other.option5) return false
        if (height != other.height) return false
        if (!stats.contentEquals(other.stats)) return false
        if (examine != other.examine) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + size
        result = 31 * result + category
        result = 31 * result + (models?.hashCode() ?: 0)
        result = 31 * result + (chatheadModels?.hashCode() ?: 0)
        result = 31 * result + standAnim
        result = 31 * result + rotateLeftAnim
        result = 31 * result + rotateRightAnim
        result = 31 * result + walkAnim
        result = 31 * result + rotateBackAnim
        result = 31 * result + walkLeftAnim
        result = 31 * result + walkRightAnim
        result = 31 * result + actions.hashCode()
        result = 31 * result + (originalColours?.hashCode() ?: 0)
        result = 31 * result + (modifiedColours?.hashCode() ?: 0)
        result = 31 * result + (originalTextureColours?.hashCode() ?: 0)
        result = 31 * result + (modifiedTextureColours?.hashCode() ?: 0)
        result = 31 * result + varbit
        result = 31 * result + varp
        result = 31 * result + (transforms?.hashCode() ?: 0)
        result = 31 * result + isMinimapVisible.hashCode()
        result = 31 * result + combatLevel
        result = 31 * result + widthScale
        result = 31 * result + heightScale
        result = 31 * result + hasRenderPriority.hashCode()
        result = 31 * result + ambient
        result = 31 * result + contrast
        result = 31 * result + (headIconArchiveIds?.hashCode() ?: 0)
        result = 31 * result + (headIconSpriteIndex?.hashCode() ?: 0)
        result = 31 * result + rotation
        result = 31 * result + isInteractable.hashCode()
        result = 31 * result + isClickable.hashCode()
        result = 31 * result + lowPriorityFollowerOps.hashCode()
        result = 31 * result + isFollower.hashCode()
        result = 31 * result + runSequence
        result = 31 * result + runBackSequence
        result = 31 * result + runRightSequence
        result = 31 * result + runLeftSequence
        result = 31 * result + crawlSequence
        result = 31 * result + crawlBackSequence
        result = 31 * result + crawlRightSequence
        result = 31 * result + crawlLeftSequence
        result = 31 * result + (params?.hashCode() ?: 0)
        result = 31 * result + inherit
        result = 31 * result + (option1?.hashCode() ?: 0)
        result = 31 * result + (option2?.hashCode() ?: 0)
        result = 31 * result + (option3?.hashCode() ?: 0)
        result = 31 * result + (option4?.hashCode() ?: 0)
        result = 31 * result + (option5?.hashCode() ?: 0)
        result = 31 * result + height
        result = 31 * result + stats.contentHashCode()
        result = 31 * result + examine.hashCode()

        return result
    }

    companion object {
        const val ATTACK = 0
        const val DEFENCE = 1
        const val STRENGTH = 2
        const val HITPOINTS = 3
        const val RANGED = 4
        const val MAGIC = 5
    }
}