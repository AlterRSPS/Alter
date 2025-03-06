//package org.alter.plugins.content.npcs.emma
//
//import org.alter.api.cfg.Varbit.MAKEOVER_INTERFACE_VIEW
//import org.alter.api.ext.*
//import org.alter.game.Server
//import org.alter.game.model.World
//import org.alter.game.model.entity.Player
//import org.alter.game.model.queue.QueueTask
//import org.alter.game.model.queue.TaskPriority
//import org.alter.game.plugin.KotlinPlugin
//import org.alter.game.plugin.PluginRepository
//import org.alter.plugins.content.interfaces.makeover.Makeover
//import org.alter.rscm.RSCM.getRSCM
//
//class Emma(
//    r: PluginRepository,
//    world: World,
//    server: Server
//) : KotlinPlugin(r, world, server) {
//    init {
//        onNpcOption("npc.emma", "haircut") {
//            player.setVarbit(MAKEOVER_INTERFACE_VIEW, 0)
//            player.queue(TaskPriority.WEAK) {
//                Makeover.open(player)
//            }
//        }
//
//        onNpcOption("npc.emma", "shave") {
//            player.setVarbit(MAKEOVER_INTERFACE_VIEW, 1)
//            player.queue(TaskPriority.WEAK) {
//                Makeover.open(player)
//            }
//        }
//        onNpcOption("npc.emma", "Talk-to") {
//            player.queue(TaskPriority.WEAK) {
//                this.chatNpc(
//                    player,
//                    "Hello. Let's get to business. I got scissors. You got hair.<br>Want to do this?",
//                    npc = getRSCM("npc.emma")
//                )
//                when (options(this)) {
//                    1 -> {
//                        this.chatPlayer(player, "I'd like a haircut please.")
//                        this.chatNpc(
//                            player,
//                            "Awesome! There are so many hair styles to pick from.<br>What would you like?",
//                            npc = getRSCM("npc.emma")
//                        )
//                        Makeover.open(player)
//                    }
//                }
//            }
//        }
//    }
//
//    suspend fun options(it: QueueTask): Int =
//        it.options(
//            player = it.ctx as Player,
//            "I'd like a haircut please.",
//            "I'd like a shave please.",
//            "Actually, I'd like to ask you about something else.",
//            "No thank you.", fullSize = true
//        )
//}
//