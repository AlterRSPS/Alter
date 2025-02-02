package org.alter.plugins.content.interfaces.emotes

import org.alter.api.EquipmentType
import org.alter.api.cfg.Varbit
import org.alter.api.ext.*
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.TaskPriority
import org.alter.rscm.RSCM.getRSCM

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EmotesTab {
    const val COMPONENT_ID = 216

    fun unlockAll(p: Player) {
        p.setVarbit(Varbit.GOBLIN_EMOTES_VARBIT, 7)
        p.setVarbit(Varbit.GLASS_BOX_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.CLIMB_ROPE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.LEAN_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.GLASS_WALL_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.IDEA_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.STAMP_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.FLAP_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.SLAP_HEAD_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.ZOMBIE_WALK_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.ZOMBIE_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.SCARED_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.RABBIT_HOP_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.EXERCISE_EMOTES, 1)
        p.setVarbit(Varbit.ZOMBIE_HAND_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.HYPERMOBILE_DRINKER_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.SKILLCAPE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.AIR_GUITAR_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.URI_TRANSFORM_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.SMOOTH_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.CRAZY_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.PREMIER_SHIELD_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.EXPLORE_VARBIT, 1)
        p.setVarbit(Varbit.FLEX_EMOTE_VARBIT, 1)
        p.setVarbit(Varbit.RELIC_UNLOCKED_EMOTE_VARBIT, 9)
        p.setVarbit(Varbit.PARTY_EMOTE_VARBIT, 1)
    }

    fun performEmote(
        p: Player,
        emote: Emote,
    ) {
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
                p.graphic("graphics.poof_disappear")
                p.setTransmogId(7311)
                wait(1)
                p.setTransmogId(7313)
                p.graphic("graphics.uri_emote_start")
                p.animate(7278)
                wait(10)
                p.animate(4069)
                wait(1)
                p.graphic("graphics.teletab_suck_in")
                p.animate(4071)
                wait(2)
                p.graphic("graphics.poof_disappear")
                p.setTransmogId(-1)
                p.unlock()
            }
        }
        /**
         * Thanks to @ClaroJack for the skill animation/gfx id's
         */
        if (emote == Emote.SKILLCAPE) {
            when (p.equipment[EquipmentType.CAPE.id]?.id) {
                getRSCM("item.max_cape_13342") -> {
                    p.animate(7121, 4)
                    p.graphic("graphics.max_cape", delay = 4)
                }
                getRSCM("item.attack_cape"), getRSCM("item.attack_capet") -> {
                    p.animate(4959)
                    p.graphic("graphics.attack_skillcape")
                }
                getRSCM("item.strength_cape"), getRSCM("item.strength_capet") -> {
                    p.animate(4981)
                    p.graphic("graphics.strength_skillcape")
                }
                getRSCM("item.defence_cape"), getRSCM("item.defence_capet") -> {
                    p.animate(4961)
                    p.graphic("graphics.defence_skillcape")
                }
                getRSCM("item.ranging_cape"), getRSCM("item.ranging_capet") -> {
                    p.animate(4973)
                    p.graphic("graphics.ranged_skillcape")
                }
                getRSCM("item.prayer_cape"), getRSCM("item.prayer_capet") -> {
                    p.animate(4979)
                    p.graphic("graphics.prayer_skillcape")
                }
                getRSCM("item.magic_cape"), getRSCM("item.magic_capet") -> {
                    p.animate(4939)
                    p.graphic("graphics.magic_skillcape")
                }
                getRSCM("item.runecraft_cape"), getRSCM("item.runecraft_capet") -> {
                    p.animate(4947)
                    p.graphic("graphics.runecrafting_skillcape")
                }
                getRSCM("item.hitpoints_cape"), getRSCM("item.hitpoints_capet") -> {
                    p.animate(4971)
                    p.graphic("graphics.hitpoints_skillcape")
                }
                getRSCM("item.agility_cape"), getRSCM("item.agility_capet") -> {
                    p.animate(4977)
                    p.graphic("graphics.agility_skillcape")
                }
                getRSCM("item.herblore_cape"), getRSCM("item.herblore_capet") -> {
                    p.animate(4969)
                    p.graphic("graphics.herblore_skillcape")
                }
                getRSCM("item.thieving_cape"), getRSCM("item.thieving_capet") -> {
                    p.animate(4965)
                    p.graphic("graphics.thieving_skillcape")
                }
                getRSCM("item.crafting_cape"), getRSCM("item.crafting_capet") -> {
                    p.animate(4949)
                    p.graphic("graphics.crafting_skillcape")
                }
                getRSCM("item.fletching_cape"), getRSCM("item.fletching_capet") -> {
                    p.animate(4937)
                    p.graphic("graphics.fletching_skillcape")
                }
                getRSCM("item.slayer_cape"), getRSCM("item.slayer_capet") -> {
                    p.animate(4967)
                    p.graphic("graphics.slayer_skillcape")
                }
                getRSCM("item.construct_cape"), getRSCM("item.construct_capet") -> {
                    p.animate(4953)
                    p.graphic("graphics.construction_skillcape")
                }
                getRSCM("item.mining_cape"), getRSCM("item.mining_capet") -> {
                    p.animate(4941)
                    p.graphic("graphics.mining_skillcape")
                }
                getRSCM("item.smithing_cape"), getRSCM("item.smithing_capet") -> {
                    p.animate(4943)
                    p.graphic("graphics.smithing_skillcape")
                }
                getRSCM("item.fishing_cape"), getRSCM("item.fishing_capet") -> {
                    p.animate(4951)
                    p.graphic("graphics.fishing_skillcape")
                }
                getRSCM("item.cooking_cape"), getRSCM("item.cooking_capet") -> {
                    p.animate(4955)
                    p.graphic("graphics.cooking_skillcape")
                }
                getRSCM("item.firemaking_cape"), getRSCM("item.firemaking_capet") -> {
                    p.animate(4975)
                    p.graphic("graphics.firemaking_skillcape")
                }
                getRSCM("item.woodcutting_cape"), getRSCM("item.woodcut_capet") -> {
                    p.animate(4957)
                    p.graphic("graphics.woodcutting_skillcape")
                }
                getRSCM("item.farming_cape"), getRSCM("item.farming_capet") -> {
                    p.animate(4963)
                    p.graphic("graphics.farming_skillcape")
                }
                getRSCM("item.hunter_cape"), getRSCM("item.hunter_capet") -> {
                    p.animate(5158)
                    p.graphic("graphics.hunter_skillcape")
                }
                getRSCM("item.cabbage_cape") -> {
                    p.animate(7209)
                }
                getRSCM("item.quest_point_cape"), getRSCM("item.quest_point_cape_t") -> {
                    p.animate(4945)
                    p.graphic("graphics.quest_point_cape")
                }
                getRSCM("item.achievement_diary_cape"), getRSCM("item.achievement_diary_cape_t") -> {
                    p.animate(2709)
                    p.graphic("graphics.achievement_diary_emote")
                }
                getRSCM("item.music_cape"), getRSCM("item.music_capet") -> {
                    p.animate(4751)
                    p.graphic("graphics.air_guitar_emote")
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
