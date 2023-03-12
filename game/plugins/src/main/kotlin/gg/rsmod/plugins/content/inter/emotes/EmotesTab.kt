package gg.rsmod.plugins.content.inter.emotes

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Varbits
import gg.rsmod.plugins.api.ext.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EmotesTab {

    const val COMPONENT_ID = 216



    fun unlockAll(p: Player) {
        p.setVarbit(Varbits.GOBLIN_EMOTES_VARBIT, 7)
        p.setVarbit(Varbits.GLASS_BOX_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.CLIMB_ROPE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.LEAN_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.GLASS_WALL_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.IDEA_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.STAMP_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.FLAP_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.SLAP_HEAD_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.ZOMBIE_WALK_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.ZOMBIE_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.SCARED_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.RABBIT_HOP_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.EXERCISE_EMOTES, 1)
        p.setVarbit(Varbits.ZOMBIE_HAND_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.HYPERMOBILE_DRINKER_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.SKILLCAPE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.AIR_GUITAR_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.URI_TRANSFORM_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.SMOOTH_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.CRAZY_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.PREMIER_SHIELD_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.EXPLORE_VARBIT, 1)
        p.setVarbit(Varbits.FLEX_EMOTE_VARBIT, 1)
        p.setVarbit(Varbits.RELIC_UNLOCKED_EMOTE_VARBIT, 9)
        p.setVarbit(Varbits.PARTY_EMOTE_VARBIT, 1)
    }

    fun performEmote(p: Player, emote: Emote) {
        if (emote.varbit != -1 && p.getVarbit(emote.varbit) != emote.requiredVarbitValue) {
            val description = emote.unlockDescription ?: "You have not unlocked this emote yet."
            p.queue { messageBox(description) }
            return
        }

        /**
         * @author Jarafi
         * If you move you get locked into uris form
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


        if (emote == Emote.RELIC_UNLOCKED) {
            p.queue(TaskPriority.STANDARD) {
                p.graphic(-1)
                p.graphic(emote.gfx, 100)
                p.unlock()
            }
        }
        if (emote.anim != -1) {
            p.queue(TaskPriority.STANDARD) {
                p.animate(-1)
                p.animate(emote.anim, 1)
                p.unlock()
            }
        }
        if (emote.gfx != -1 && emote != Emote.RELIC_UNLOCKED) {
            p.queue(TaskPriority.STANDARD) {
                p.graphic(-1)
                p.graphic(emote.gfx)
                p.unlock()
            }
        }
    }
}