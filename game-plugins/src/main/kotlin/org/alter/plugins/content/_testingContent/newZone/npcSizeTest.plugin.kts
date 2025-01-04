import dev.openrune.cache.CacheManager
import org.alter.api.cfg.Graphic
import org.alter.plugins.content.combat.*
import org.alter.rscm.RSCM.getRSCM

/**
 * @author CloudS3c 12/28/2024
 */

val corrupted_hunllef = "npc.corrupted_hunllef"


spawnNpc(corrupted_hunllef, Tile(1823, 3116))

onNpcCombat(corrupted_hunllef) {
    npc.queue {
        combat(this)
    }
}

setCombatDef("npc.corrupted_hunllef") {
    configs {
        attackSpeed = 5
        respawnDelay = 15
    }

    aggro {
        radius = 16
        searchDelay = 1
    }

    stats {
        hitpoints = 1000
        attack = 240
        strength = 240
        defence = 240
        magic = 240
        ranged = 240
    }

    bonuses {
        attackBonus = 90
        strengthBonus = 112
        attackMagic = 90
        magicDamageBonus = 112
        attackRanged = 90
        rangedStrengthBonus = 112
        // @TODO Redo defence and add DSL
    }

    anims {
        //block = Animation.
        //death = 92
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    val target = npc.getCombatTarget() ?: return
    var projectile_count = 0

    while(npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 10, projectile = true) && npc.isAttackDelayReady()) {
            if (npc.getCurrentHp() < 250) {

            }
        }
    }
}
