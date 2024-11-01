import org.alter.plugins.content.combat.canEngageCombat
import org.alter.plugins.content.combat.getCombatTarget
import org.alter.plugins.content.combat.isAttackDelayReady
import org.alter.plugins.content.combat.moveToAttackRange

/**
 * @author CloudS3c 10/28/2024
 */
    
val tar = Npcs.VAMPYRE_JUVINATE_3698

spawn_npc(Npcs.KING_BLACK_DRAGON, 3170, 3490)

spawn_npc(tar, 3176, 3480)

set_combat_def(tar) {
    /**
     * @TODO
     * java.lang.reflect.InvocationTargetException
     * 	at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
     * 	at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:77)
     * 	at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
     * 	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499)
     * 	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:480)
     * 	at org.alter.game.plugin.PluginRepository.scanPackageForPlugins(PluginRepository.kt:455)
     * 	at org.alter.game.plugin.PluginRepository.loadPlugins(PluginRepository.kt:433)
     * 	at org.alter.game.plugin.PluginRepository.init(PluginRepository.kt:421)
     * 	at org.alter.game.Server.startGame(Server.kt:172)
     * 	at org.alter.game.Launcher.main(Launcher.kt:10)
     * Caused by: java.lang.IllegalStateException: Attack speed must be set.
     * 	at org.alter.api.NpcCombatBuilder.build(NpcParams.kt:122)
     * 	at org.alter.api.dsl.NpcCombatDsl$Builder.build(NpcCombatDsl.kt:36)
     * 	at org.alter.api.dsl.NpcCombatDslKt.set_combat_def(NpcCombatDsl.kt:18)
     * 	at Range_tester_plugin.<init>(range_tester.plugin.kts:8)
     * 	... 10 more
     * Caused by: java.lang.IllegalStateException: Attack speed must be set.
     *
     *
     * @TODO Check if it would be possible to make Intellij give error before launching the server.
     */
    stats {
        hitpoints = 40
        attack = -100
        strength = -100
        defence = -100
        magic = -100
        ranged = -100
    }
    configs {
        attackSpeed = 4
        respawnDelay = 7

    }
    anims {
        attack = Animation.VAMPIRE_ATTACK
        block = Animation.VAMPIRE_SUCK
        death = Animation.VAMPIRE_CAPTURED
    }

    sound {
        attackSound = Sound.VAMPIRE_ATTACK
        blockSound = Sound.VAMPIRE_SUCK
        deathSound = Sound.VAMPIRE_DEATH
    }
}
on_npc_combat(tar) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 6, projectile = true) && npc.isAttackDelayReady()) {
            npc.facePawn(target)
            npc.animate(Animation.THROW_SNOWBALL_OLD)
        }
        target = npc.getCombatTarget() ?: break
        it.wait(1)
    }
}