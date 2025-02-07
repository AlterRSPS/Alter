package org.alter.game.service.game

import AnimationData
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.openrune.cache.CacheManager
import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.filestore.definition.data.ItemType
import dev.openrune.cache.filestore.definition.data.ParamMapper
import gg.rsmod.util.ServerProperties
import gg.rsmod.util.Stopwatch
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import org.yaml.snakeyaml.LoaderOptions
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

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
            /**
             * Loads item examine text from an external CSV file and assigns it to item definitions.
             *
             * The file is expected to be located at `../data/cfg/objs.csv` and should contain item IDs
             * paired with their respective examine text, separated by commas.
             *
             * - The first value in each line is treated as the item ID.
             * - The remaining text after the first comma is treated as the examine description.
             * - The examine text is assigned to the corresponding item definition if the ID is valid.
             *
             * This ensures that item examine information gets loaded from an external source at runtime.
             */
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

            /**
             * Initializes item definitions by loading cached item configurations and updating specific attributes.
             *
             * - Adjusts item weight by dividing the cached value by 1000.
             * - Sets the attack speed using a validated parameter (ID 14).
             * - Determines the weapon type for equippable items in the weapon slot (equipSlot 3) based on their category.
             * - Assigns the equip type from the item's appearance override.
             * - Populates item bonuses using a predefined set of validated parameters.
             *
             * This process ensures that item attributes are properly loaded and validated from cache for use in gameplay.
             */
            CacheManager.getItems().forEach { (_, item) ->
                val def = getItem(item.id)

                def.weight /= 1000
                def.equipType = def.appearanceOverride1

                def.attackSpeed = def.getValidatedParam(
                    ParamMapper.item.ATTACK_RATE,
                    7
                ) // Just in case the Attack Rate would be not configurated in cache.

                if (def.equipSlot == 3) {
                    def.weaponType = WeaponCategory.get(def, def.category)
                }


                def.bonuses =
                    intArrayOf(
                        def.getValidatedParam(ParamMapper.item.STAB_ATTACK_BONUS),
                        def.getValidatedParam(ParamMapper.item.SLASH_ATTACK_BONUS),
                        def.getValidatedParam(ParamMapper.item.CRUSH_ATTACK_BONUS),
                        def.getValidatedParam(ParamMapper.item.MAGIC_ATTACK_BONUS),
                        def.getValidatedParam(ParamMapper.item.RANGED_ATTACK_BONUS),
                        def.getValidatedParam(ParamMapper.item.STAB_DEFENCE_BONUS),
                        def.getValidatedParam(ParamMapper.item.SLASH_DEFENCE_BONUS),
                        def.getValidatedParam(ParamMapper.item.CRUSH_DEFENCE_BONUS),
                        def.getValidatedParam(ParamMapper.item.MAGIC_DEFENCE_BONUS),
                        def.getValidatedParam(ParamMapper.item.RANGED_DEFENCE_BONUS),
                        def.getValidatedParam(ParamMapper.item.MELEE_STRENGTH),
                        def.getValidatedParam(ParamMapper.item.RANGED_STRENGTH_BONUS),
                        def.getValidatedParam(ParamMapper.item.MAGIC_DAMAGE_STRENGTH) / 10,
                        def.getValidatedParam(ParamMapper.item.PRAYER_BONUS),
                    )

                if (def.params?.containsKey(ParamMapper.item.PRIMARY_SKILL) == true) {
                    def.skillReqs = Byte2ByteOpenHashMap().apply {
                        put(
                            def.getValidatedParam(ParamMapper.item.PRIMARY_SKILL).toByte(),
                            def.getValidatedParam(ParamMapper.item.PRIMARY_LEVEL).toByte()
                        )
                        put(
                            def.getValidatedParam(ParamMapper.item.SECONDARY_SKILL).toByte(),
                            def.getValidatedParam(ParamMapper.item.SECONDARY_LEVEL).toByte()
                        )
                        put(
                            def.getValidatedParam(ParamMapper.item.TERTIARY_SKILL).toByte(),
                            def.getValidatedParam(ParamMapper.item.TERTIARY_LEVEL).toByte()
                        )
                        put(
                            def.getValidatedParam(ParamMapper.item.QUATERNARY_SKILL).toByte(),
                            def.getValidatedParam(ParamMapper.item.QUATERNARY_LEVEL).toByte()
                        )
                    }
                }
            }

            /**
             * Loads and assigns render animations to item definitions from external JSON files.
             *
             * - `bas_mappings.json` maps animation identifiers to their corresponding animation data (e.g., ready, walk, run animations).
             * - `item_bas.json` maps item IDs to the animation identifiers used in the mappings.
             *
             * The process:
             * - Each item ID from `item_bas.json` is matched to its animation data from `bas_mappings.json`.
             * - If a matching animation is found, it populates the item's render animations array with the relevant animation IDs.
             *
             * This ensures that items have appropriate movement and action animations during gameplay.
             */
            val animationMap: Map<String, AnimationData> =
                mapper.readValue(File("../data/cfg/items/renderAnimations/bas_mappings.json").readText())
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

            /**
             * Loads item override metadata from all files within the "itemOverrides" directory.
             *
             * - The directory is resolved relative to the provided path.
             * - Files are processed in parallel for efficient loading.
             * - Each file is deserialized into a `Metadata` object and passed to the `load` function.
             *
             * This process ensures that custom item attributes or behaviors are loaded at runtime.
             *
             * @TODO Add better context as to why file could not be loaded.
             * @TODO Add support for remaining [`def`] properties override method.
             */
            Files.walk(path.resolve("itemOverrides")).parallel().filter { it.toFile().isFile }.forEach { file ->
                if (file.fileName.toString().contains("FileExample.yml")) return@forEach

                val content = file.toFile().readText()
                content.split(Regex("(?m)^---\\s*$"))
                    .filter { it.isNotBlank() }.forEach { document ->
                        val data = mapper.readValue(document, Metadata::class.java)
                        load(data)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ms = stopwatch.elapsed(TimeUnit.MILLISECONDS)
    }

    fun load(item: Metadata) {
        val def = getItem(item.id)

        def.name = item.name
        def.examine = item.examine ?: ""
        def.isTradeable = item.tradeable
        def.weight = item.weight

        if (item.equipment != null) {
            val equipment = item.equipment
            val slots = if (equipment.equipSlot != null) getEquipmentSlots(equipment.equipSlot, def.id) else null

            def.attackSpeed = equipment.attackSpeed

            if (equipment.weaponType == -1 && slots != null) {
                if (slots.slot == 3) def.weaponType = 17
            } else {
                def.weaponType = equipment.weaponType
            }

            def.renderAnimations = equipment.renderAnimations?.getAsArray()
            /**
             * TODO def.attackSounds = equipment.attackSounds
             *  - Create Array of AttackStyleID -> It's Sound
             *  accurateAnim : accurateSound
             *  aggressiveAnim : aggressiveSound
             *  controlledAnim : controlledSound
             *  defensiveAnim : defensiveSound
             *  <--- AttackStyleID:[Anim, Sound]
             *  AttackStyle can be from 0-3
             *  If no data on it, it will be -1
             *  blockAnim = When target attacks the Pawn on next tick?
             *
             *
             *  TODO def.equipSound = equipment.equipSound
             */
            if (slots != null) {
                def.equipSlot = slots.slot
                def.equipType = slots.secondary
            }

            if (equipment.skillReqs != null) {
                val reqs = Byte2ByteOpenHashMap()
                equipment.skillReqs.filter { it.skill != null }.forEach { req ->
                    reqs[getSkillId(req.skill!!)] = req.level!!.toByte()
                }

                def.skillReqs = reqs
            }

            def.bonuses = intArrayOf(
                equipment.attackStab,
                equipment.attackSlash,
                equipment.attackCrush,
                equipment.attackMagic,
                equipment.attackRanged,
                equipment.defenceStab,
                equipment.defenceSlash,
                equipment.defenceCrush,
                equipment.defenceMagic,
                equipment.defenceRanged,
                equipment.meleeStrength,
                equipment.rangedStrength,
                equipment.magicDamage,
                equipment.prayer,
            )
        }
    }

    private fun getEquipmentSlots(
        slot: String,
        id: Int? = null,
    ): EquipmentSlots {
        val equipSlot: Int
        var equipType = -1
        when (slot) {
            "hat" -> equipSlot = 0
            "cape" -> equipSlot = 1
            "neck" -> equipSlot = 2
            "weapon" -> equipSlot = 3
            "torso" -> equipSlot = 4
            "shield" -> equipSlot = 5
            "legs" -> equipSlot = 7
            "hands" -> equipSlot = 9
            "feet" -> equipSlot = 10
            "ring" -> equipSlot = 12
            "ammo" -> equipSlot = 13

            "head" -> {
                equipSlot = 0
                equipType = 8
            }
            // For hats that requires hair removal
            "nohair" -> {
                equipSlot = 0
                equipType = 11
            }

            "2h" -> {
                equipSlot = 3
                equipType = 5
            }

            "body" -> {
                equipSlot = 4
                equipType = 6
            }

            else -> throw IllegalArgumentException("Illegal equipment slot: $slot, $id")
        }
        return EquipmentSlots(equipSlot, equipType)
    }

    private data class EquipmentSlots(val slot: Int, val secondary: Int)


    private fun ItemType.getValidatedParam(key: Int, defaultValue: Int = 0): Int {
        if (this.params?.get(key) != null) {
            try {
                return this.params?.get(key) as Int
            } catch (e: Exception) {
                println("${this.id} || ${this.params}")
                e.printStackTrace()
            }
        }

        /**
         * @TODO Rethink the logic, gets printed out even for items that are not wearable.
         * logger.warn {
         *   "Item with ID: ${this.id} is missing the key '$key' in its params. Full params list: ${this.params}. Default value was set: $defaultValue."
         * }
         */
        return defaultValue
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
        @JsonProperty("render_animations") val renderAnimations: RenderAnimations? = null,
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

    data class RenderAnimations(
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

    companion object {
        val logger = KotlinLogging.logger {}
    }
}
