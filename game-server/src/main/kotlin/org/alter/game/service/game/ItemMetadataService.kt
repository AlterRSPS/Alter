package org.alter.game.service.game

import AnimationData
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.openrune.cache.CacheManager
import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.filestore.definition.data.ItemType
import gg.rsmod.util.ServerProperties
import gg.rsmod.util.Stopwatch
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import org.yaml.snakeyaml.LoaderOptions
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import com.fasterxml.jackson.databind.ObjectMapper
/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemMetadataService : Service {
    override fun init(
        server: Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        loadAll()
    }

    var ms: Long = 0
    fun loadAll() {
        val stopwatch = Stopwatch.createStarted().reset().start()
        val loaderOptions = LoaderOptions()
        loaderOptions.codePointLimit = 10 * 1024 * 1024 // 10 MB
        val yamlFactory =
            YAMLFactory.builder()
                .loaderOptions(loaderOptions)
                .build()
        val mapper = YAMLMapper(yamlFactory)

        val path = Paths.get("../data/cfg/items")

        try {
            Paths.get("../data/cfg/objs.csv").toFile().forEachLine { line ->
                val parts = line.split(",")
                if (parts.size >= 2) {
                    val id = parts[0].toIntOrNull()
                    val examine = line.substringAfter(',').trim()
                    if (id != null) {
                        getItem(id).examine = examine
                    }
                }
            }
            CacheManager.getItems().forEach { _, item ->
                val def = getItem(item.id)
                def.weight /= 1000 // @TODO Need to check this out
                def.attackSpeed = def.getValidatedParam(14)
                if (def.equipSlot == 3) {
                    def.weaponType = WeaponCategory.get(def, def.category)
                }
                def.equipType = def.appearanceOverride1
                def.bonuses =
                    intArrayOf(
                        def.getValidatedParam(0),
                        def.getValidatedParam(1),
                        def.getValidatedParam(2),
                        def.getValidatedParam(3),
                        def.getValidatedParam(4),
                        def.getValidatedParam(5),
                        def.getValidatedParam(6),
                        def.getValidatedParam(7),
                        def.getValidatedParam(8),
                        def.getValidatedParam(9),
                        def.getValidatedParam(10),
                        def.getValidatedParam(12),
                        def.getValidatedParam(65),
                        def.getValidatedParam(11),
                    )
            }
            Files.walk(path.resolve("equippable")).parallel().filter { it.toFile().isFile }.forEach {
                val data = mapper.readValue(it.toFile(), Metadata::class.java)
                // Need to cleanup old mess so load w.e we need to keep and save into new YML File || Or better TOML :S and later delete
                load(data)
            }
            val animationMap: Map<String, AnimationData> = mapper.readValue(File("../data/cfg/items/renderAnimations/bas_mappings.json").readText())
            val valueMap: Map<Int, Int> = ObjectMapper().apply {
                findAndRegisterModules()
            }.readValue(File("../data/cfg/items/renderAnimations/item_bas.json").readText())
            valueMap.forEach { (item, animMap) ->
                val animation = animationMap[animMap.toString()] ?: return@forEach
                val def = getItem(item)
                def.renderAnimations = intArrayOf(
                    animation.readyAnim,
                    animation.turnAnim,
                    animation.walkAnim,
                    animation.walkAnimBack,
                    animation.walkAnimLeft,
                    animation.walkAnimRight,
                    animation.runAnim,
                )
            }
        } catch (e: Exception) {
            throw e
        }
        ms = stopwatch.elapsed(TimeUnit.MILLISECONDS)
    }
    fun load(item: Metadata) {
        val def = getItem(item.id)
        if (item.equipment != null) {
            val equipment = item.equipment
            // TODO def.equipSound = equipment.equipSound
            // TODO def.attackSounds = equipment.attackSounds
            // TODO Map the magic numbers out right now it looks like shit and confusing.
            //def.renderAnimations = equipment.renderAnimations?.getAsArray()
            if (item.equipment.skillReqs != null) {
                val reqs = Byte2ByteOpenHashMap()
                item.equipment.skillReqs.filter { it.skill != null }.forEach { req ->
                    reqs[getSkillId(req.skill!!)] = req.level!!.toByte()
                }
                def.skillReqs = reqs
            }
        }
    }

    private fun ItemType.getValidatedParam(key: Int): Int {
        if (this.params?.get(key) != null) {
            try {
                return this.params?.get(key) as Int
            } catch (e: Exception) {
                println("${this.id} || ${this.params}")
                e.printStackTrace()
            }
        }
        return 0
    }

    private fun getSkillId(name: String): Byte =
        when (name) {
            // Need to get a better dump db. As we can see, this one has some
            // inconsistency for some reason.
            "attack" -> 0
            "defence" -> 1
            "strength" -> 2
            "hitpoints" -> 3
            "range", "ranged" -> 4
            "prayer" -> 5
            "magic" -> 6
            "cooking" -> 7
            "woodcutting" -> 8
            "fletching" -> 9
            "fishing" -> 10
            "firemaking" -> 11
            "crafting" -> 12
            "smithing" -> 13
            "mining" -> 14
            "herblore" -> 15
            "agility" -> 16
            "thieving", "theiving" -> 17
            "slayer" -> 18
            "farming" -> 19
            "runecrafting", "runecraft" -> 20
            "hunter" -> 21
            "construction", "contruction" -> 22
            "combat" -> 3
            else -> throw IllegalArgumentException("Illegal skill name: $name")
        }

    data class Metadata(
        val id: Int = -1,
        val name: String = "",
        val examine: String? = null,
        val tradeable: Boolean = false,
        val weight: Double = 0.0,
        val tradeable_on_ge: Boolean = false,
        val cost: Int = 0,
        val lowalch: Int = 0,
        val highalch: Int = 0,
        val buy_limit: Int? = null,
        val equipment: Equipment? = null,
    )

    data class Equipment(
        @JsonProperty("equip_slot") val equipSlot: String? = null,
        @JsonProperty("equip_sound") val equipSound: Int? = -1,
        @JsonProperty("weapon_type") val weaponType: Int = -1,
        @JsonProperty("attack_speed") val attackSpeed: Int = -1,
        @JsonProperty("attack_stab") val attackStab: Int = 0,
        @JsonProperty("attack_slash") val attackSlash: Int = 0,
        @JsonProperty("attack_crush") val attackCrush: Int = 0,
        @JsonProperty("attack_magic") val attackMagic: Int = 0,
        @JsonProperty("attack_ranged") val attackRanged: Int = 0,
        @JsonProperty("defence_stab") val defenceStab: Int = 0,
        @JsonProperty("defence_slash") val defenceSlash: Int = 0,
        @JsonProperty("defence_crush") val defenceCrush: Int = 0,
        @JsonProperty("defence_magic") val defenceMagic: Int = 0,
        @JsonProperty("defence_ranged") val defenceRanged: Int = 0,
        @JsonProperty("melee_strength") val meleeStrength: Int = 0,
        @JsonProperty("ranged_strength") val rangedStrength: Int = 0,
        @JsonProperty("magic_damage") val magicDamage: Int = 0,
        @JsonProperty("prayer") val prayer: Int = 0,
        @JsonProperty("render_animations") val renderAnimations: renderAnimations? = null,
        @JsonProperty("attackSounds") val attackSounds: IntArray? = null,
        @JsonProperty("skill_reqs") val skillReqs: Array<SkillRequirement>? = null,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Equipment
            if (equipSlot != other.equipSlot) return false
            if (equipSound != other.equipSound) return false
            if (weaponType != other.weaponType) return false
            if (attackSpeed != other.attackSpeed) return false
            if (attackStab != other.attackStab) return false
            if (attackSlash != other.attackSlash) return false
            if (attackCrush != other.attackCrush) return false
            if (attackMagic != other.attackMagic) return false
            if (attackRanged != other.attackRanged) return false
            if (defenceStab != other.defenceStab) return false
            if (defenceSlash != other.defenceSlash) return false
            if (defenceCrush != other.defenceCrush) return false
            if (defenceMagic != other.defenceMagic) return false
            if (defenceRanged != other.defenceRanged) return false
            if (meleeStrength != other.meleeStrength) return false
            if (rangedStrength != other.rangedStrength) return false
            if (magicDamage != other.magicDamage) return false
            if (prayer != other.prayer) return false

            if (renderAnimations != null) {
                if (other.renderAnimations == null) return false
            } else if (other.renderAnimations != null) {
                return false
            }

            if (attackSounds != null) return false

            if (skillReqs != null) {
                if (other.skillReqs == null) return false
                if (!skillReqs.contentEquals(other.skillReqs)) return false
            } else if (other.skillReqs != null) {
                return false
            }

            return true
        }

        override fun hashCode(): Int {
            var result = equipSlot?.hashCode() ?: 0
            result = 31 * result + weaponType
            result = 31 * result + attackSpeed
            result = 31 * result + attackStab
            result = 31 * result + attackSlash
            result = 31 * result + attackCrush
            result = 31 * result + attackMagic
            result = 31 * result + attackRanged
            result = 31 * result + defenceStab
            result = 31 * result + defenceSlash
            result = 31 * result + defenceCrush
            result = 31 * result + defenceMagic
            result = 31 * result + defenceRanged
            result = 31 * result + meleeStrength
            result = 31 * result + rangedStrength
            result = 31 * result + magicDamage
            result = 31 * result + prayer
            result = 31 * result + (renderAnimations?.getAsArray().contentHashCode())
            result = 31 * result + (skillReqs?.contentHashCode() ?: 0)
            return result
        }
    }

    data class renderAnimations(
        @JsonProperty("standAnimId") val standAnimId: Int = 0,
        @JsonProperty("turnOnSpotAnim") val turnOnSpotAnim: Int = 0,
        @JsonProperty("walkForwardAnimId") val walkForwardAnimId: Int = 0,
        @JsonProperty("walkBackwardsAnimId") val walkBackwardsAnimId: Int = 0,
        @JsonProperty("walkLeftAnimId") val walkLeftAnimId: Int = 0,
        @JsonProperty("walkRightAnimId") val walkRightAnimId: Int = 0,
        @JsonProperty("runAnimId") val runAnimId: Int = 0,
    ) {
        fun getAsArray(): IntArray {
            return listOf(
                standAnimId,
                turnOnSpotAnim,
                walkForwardAnimId,
                walkBackwardsAnimId,
                walkLeftAnimId,
                walkRightAnimId,
                runAnimId
            ).toIntArray()
        }
    }

    data class SkillRequirement(
        @JsonProperty("skill") val skill: String?,
        @JsonProperty("level") val level: Int?,
    )
}
