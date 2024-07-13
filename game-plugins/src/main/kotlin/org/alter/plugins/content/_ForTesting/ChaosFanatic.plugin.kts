//import org.alter.plugins.content.combat.canEngageCombat
//import org.alter.plugins.content.combat.getCombatTarget
//import org.alter.plugins.content.combat.isAttackDelayReady
//import org.alter.plugins.content.combat.moveToAttackRange
//
///**
// * @author CloudS3c
// */
//
//val ChaosFanatic_Message = arrayListOf(
//    "BURN!",
//    "WEUGH!",
//    "Devilish Oxen Roll!",
//    "All your wilderness are belong to them!",
//    "AhehHeheuhHhahueHuUEehEahAH",
//    "I shall call him squidgy and he shall be my squidgy!"
//)
//
//val ChaosFanatic = Npcs.CHAOS_FANATIC
//
//on_npc_combat(ChaosFanatic) {
//    npc.queue {
//        combat(this)
//    }
//}
//
//suspend fun combat(it: QueueTask) {
//    val npc = it.npc
//    val target = npc.getCombatTarget() ?: return
//
//    while (npc.canEngageCombat(target)) {
//        npc.facePawn(target)
//        if (npc.moveToAttackRange(it, target, 6, projectile = true) and npc.isAttackDelayReady()) {
//            when (world.random(0..1)) {
//                0 -> basicAttack(npc, target)
//            }
//        }
//    }
//}
//
//fun basicAttack(npc: Npc, target: Pawn) {
//}
//
//
//set_combat_def(ChaosFanatic) {
//
//}