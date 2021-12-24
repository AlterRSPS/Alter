package gg.rsmod.plugins.content.inter.emotes

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.getVarbit
import gg.rsmod.plugins.api.ext.messageBox
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.setVarbit
import kotlinx.coroutines.channels.ticker

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EmotesTab {

    const val COMPONENT_ID = 216

    const val GOBLIN_EMOTES_VARBIT = 532
    const val GLASS_BOX_EMOTE_VARBIT = 1368
    const val CLIMB_ROPE_EMOTE_VARBIT = 1369
    const val LEAN_EMOTE_VARBIT = 1370
    const val GLASS_WALL_EMOTE_VARBIT = 1367
    const val IDEA_EMOTE_VARBIT = 2311
    const val STAMP_EMOTE_VARBIT = 2312
    const val FLAP_EMOTE_VARBIT = 2309
    const val SLAP_HEAD_EMOTE_VARBIT = 2310
    const val ZOMBIE_WALK_EMOTE_VARBIT = 1921
    const val ZOMBIE_DANCE_EMOTE_VARBIT = 1920
    const val SCARED_EMOTE_VARBIT = 1371
    const val RABBIT_HOP_EMOTE_VARBIT = 2055
    const val EXERCISE_EMOTES = 4732
    const val ZOMBIE_HAND_EMOTE_VARBIT = 1000
    const val HYPERMOBILE_DRINKER_EMOTE_VARBIT = 4802
    const val SKILLCAPE_EMOTE_VARBIT = 4797
    const val AIR_GUITAR_EMOTE_VARBIT = 4673
    const val URI_TRANSFORM_EMOTE_VARBIT = 5104
    const val SMOOTH_DANCE_EMOTE_VARBIT = 5597
    const val CRAZY_DANCE_EMOTE_VARBIT = 5598
    const val PREMIER_SHIELD_EMOTE_VARBIT = 6041
    const val EXPLORE_VARBIT = 10115
    const val FLEX_EMOTE_VARBIT = 12062
    const val RELIC_UNLOCKED_EMOTE_VARBIT = 11757

    fun unlockAll(p: Player) {
        p.setVarbit(GOBLIN_EMOTES_VARBIT, 7)
        p.setVarbit(GLASS_BOX_EMOTE_VARBIT, 1)
        p.setVarbit(CLIMB_ROPE_EMOTE_VARBIT, 1)
        p.setVarbit(LEAN_EMOTE_VARBIT, 1)
        p.setVarbit(GLASS_WALL_EMOTE_VARBIT, 1)
        p.setVarbit(IDEA_EMOTE_VARBIT, 1)
        p.setVarbit(STAMP_EMOTE_VARBIT, 1)
        p.setVarbit(FLAP_EMOTE_VARBIT, 1)
        p.setVarbit(SLAP_HEAD_EMOTE_VARBIT, 1)
        p.setVarbit(ZOMBIE_WALK_EMOTE_VARBIT, 1)
        p.setVarbit(ZOMBIE_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(SCARED_EMOTE_VARBIT, 1)
        p.setVarbit(RABBIT_HOP_EMOTE_VARBIT, 1)
        p.setVarbit(EXERCISE_EMOTES, 1)
        p.setVarbit(ZOMBIE_HAND_EMOTE_VARBIT, 1)
        p.setVarbit(HYPERMOBILE_DRINKER_EMOTE_VARBIT, 1)
        p.setVarbit(SKILLCAPE_EMOTE_VARBIT, 1)
        p.setVarbit(AIR_GUITAR_EMOTE_VARBIT, 1)
        p.setVarbit(URI_TRANSFORM_EMOTE_VARBIT, 1)
        p.setVarbit(SMOOTH_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(CRAZY_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(PREMIER_SHIELD_EMOTE_VARBIT, 1)
        p.setVarbit(EXPLORE_VARBIT, 1)
        p.setVarbit(FLEX_EMOTE_VARBIT, 1)
        p.setVarbit(RELIC_UNLOCKED_EMOTE_VARBIT, 1)
    }

    fun performEmote(p: Player, emote: Emote) {
        if (emote.varbit != -1 && p.getVarbit(emote.varbit) != emote.requiredVarbitValue) {
            val description = emote.unlockDescription ?: "You have not unlocked this emote yet."
            p.queue { messageBox(description) }
            return
        }
        /**
         * @author Jarafi
         */
        if (emote == Emote.URI_TRANSFORM) {
            p.queue {
                p.lock()
                p.graphic(86)
                p.setTransmogId(7311)
                wait(1)
                p.setTransmogId(7313)
                p.graphic(1306)
                p.animate(7278)
                wait(10)
                p.animate(4069)
                wait(1)
                p.graphic(678)
                p.animate(4071)
                wait(2)
                p.graphic(86)
                p.setTransmogId(-1)
                p.unlock()
            }
        }
        /**
         * Thanks to @ClaroJack for the skill animation/gfx id's
         * @TODO Need to lock the players state when he performs One of the max capes animation
         */
        if (emote == Emote.SKILLCAPE) {
            when(p.equipment[EquipmentType.CAPE.id]?.id) {
                Items.MAX_CAPE_13342 -> {
                    p.animate(7121, 4)
                    p.graphic(1286, delay = 4)
                }
                Items.ATTACK_CAPE, Items.ATTACK_CAPET -> {
                    p.animate(4959)
                    p.graphic(823)
                }
                Items.STRENGTH_CAPE, Items.STRENGTH_CAPET -> {
                    p.animate(4981)
                    p.graphic(828)
                }
                Items.DEFENCE_CAPE, Items.DEFENCE_CAPET -> {
                    p.animate(4961)
                    p.graphic(824)
                }
                Items.RANGING_CAPE, Items.RANGING_CAPET -> {
                    p.animate(4973)
                    p.graphic(832)
                }
                Items.PRAYER_CAPE, Items.PRAYER_CAPET -> {
                    p.animate(4979)
                    p.graphic(829)
                }
                Items.MAGIC_CAPE, Items.MAGIC_CAPET -> {
                    p.animate(4939)
                    p.graphic(813)
                }
                Items.RUNECRAFT_CAPE, Items.RUNECRAFT_CAPET -> {
                    p.animate(4947)
                    p.graphic(817)
                }
                Items.HITPOINTS_CAPE, Items.HITPOINTS_CAPET -> {
                    p.animate(4971)
                    p.graphic(833)
                }
                Items.AGILITY_CAPE, Items.AGILITY_CAPET -> {
                    p.animate(4977)
                    p.graphic(830)
                }
                Items.HERBLORE_CAPE, Items.HERBLORE_CAPET -> {
                    p.animate(4969)
                    p.graphic(835)
                }
                Items.THIEVING_CAPE, Items.THIEVING_CAPET -> {
                    p.animate(4965)
                    p.graphic(826)
                }
                Items.CRAFTING_CAPE, Items.CRAFTING_CAPET -> {
                    p.animate(4949)
                    p.graphic(818)
                }
                Items.FLETCHING_CAPE, Items.FLETCHING_CAPET -> {
                    p.animate(4937)
                    p.graphic(812)
                }
                Items.SLAYER_CAPE, Items.SLAYER_CAPET -> {
                    p.animate(4967)
                    p.graphic(827)
                }
                Items.CONSTRUCT_CAPE, Items.CONSTRUCT_CAPET -> {
                    p.animate(4953)
                    p.graphic(820)
                }
                Items.MINING_CAPE, Items.MINING_CAPET -> {
                    p.animate(4941)
                    p.graphic(814)
                }
                Items.SMITHING_CAPE, Items.SMITHING_CAPET -> {
                    p.animate(4943)
                    p.graphic(815)
                }
                Items.FISHING_CAPE, Items.FISHING_CAPET -> {
                    p.animate(4951)
                    p.graphic(819)
                }
                Items.COOKING_CAPE, Items.COOKING_CAPET -> {
                    p.animate(4955)
                    p.graphic(821)
                }
                Items.FIREMAKING_CAPE, Items.FIREMAKING_CAPET -> {
                    p.animate(4975)
                    p.graphic(831)
                }
                Items.WOODCUTTING_CAPE, Items.WOODCUT_CAPET -> {
                    p.animate(4957)
                    p.graphic(822)
                }
                Items.FARMING_CAPE, Items.FARMING_CAPET -> {
                    p.animate(4963)
                    p.graphic(825)
                }
                Items.HUNTER_CAPE, Items.HUNTER_CAPET -> {
                    p.animate(5158)
                    p.graphic(907)
                }
                Items.CABBAGE_CAPE -> {
                    p.animate(7209)
                }
                Items.QUEST_POINT_CAPE, Items.QUEST_POINT_CAPE_T -> {
                    p.animate(4945)
                    p.graphic(816)
                }
                Items.ACHIEVEMENT_DIARY_CAPE, Items.ACHIEVEMENT_DIARY_CAPE_T -> {
                    p.animate(2709)
                    p.graphic(323)
                }
                Items.MUSIC_CAPE, Items.MUSIC_CAPET -> {
                    p.animate(4751)
                    p.graphic(1239)
                }
            }
        }
        if (emote.anim != -1) {
            p.queue(TaskPriority.STANDARD) {
                p.animate(-1)
                p.lock()
                p.stopMovement()
                p.animate(emote.anim, 1)
                p.unlock()
            }
        }
        if (emote.gfx != -1) {
            p.queue(TaskPriority.STANDARD) {
                p.graphic(-1)
                p.lock()
                p.stopMovement()
                p.graphic(emote.gfx)
                p.unlock()
            }
        }
    }
}